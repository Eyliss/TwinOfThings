package com.twinofthings.sezamecore.api;

import com.twinofthings.BuildConfig;
import com.twinofthings.sezamecore.api.repositories.DeviceRepository;
import com.twinofthings.sezamecore.api.repositories.EventRepository;
import com.twinofthings.sezamecore.api.repositories.UserRepository;
import com.twinofthings.sezamecore.api.services.DeviceRestService;
import com.twinofthings.sezamecore.api.services.EventRestService;
import com.twinofthings.sezamecore.api.services.UserRestService;
import com.twinofthings.sezamecore.schedulers.SchedulerProvider;
import com.twinofthings.sezamecore.security.SslUtilities;
import com.twinofthings.sezamecore.utils.P;
import com.twinofthings.sezamecore.utils.S;

import java.io.IOException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

/**
 * @author Felix Tutzer
 *         © Nous 2017
 *         <p>
 *         <p>
 *         Api layer
 */

public class SezameDataProvider {

    private static final Headers regularHeaders = Headers.of("User-Agent", "Sezame", "Content-Type", "application/json;charset=UTF-8");

    private final RxServiceComposer rxServiceComposer =
            new RxServiceComposer(new SchedulerProvider());

    private static SezameDataProvider instance;

    private Retrofit userRegisterRetrofitInstance;
    private Retrofit deviceRegisterRetrofitInstance;
    private Retrofit commonRetrofitInstance;
    private SSLSocketFactory socketFactory;
    private X509TrustManager trustManager;

    private UserRepository userRepository;
    private UserRepository userRegisterRepository;
    private EventRepository eventRepository;
    private DeviceRepository deviceRepository;
    private DeviceRepository deviceRegisterRepository;

    private String url;

    private SezameDataProvider() {
        setBaseUrl();
    }

    private void setBaseUrl() {
        if (S.isBlank(url)) {
            if (S.isBlank(P.instance().getString(P.BASE_URL))) {
                P.instance().putString(P.BASE_URL, BuildConfig.TEST_URL);
            }
            url = P.instance().getString(P.BASE_URL);
        }
    }

    //region PUBLIC

    public static SezameDataProvider instance() {
        if (SezameDataProvider.instance == null) {
            SezameDataProvider.instance = new SezameDataProvider();
            SezameDataProvider.instance.setupSocketFactoryAndTrustManager();
        }
        return SezameDataProvider.instance;
    }

    private void setupSocketFactoryAndTrustManager() {
        X509TrustManager trustManager = SslUtilities.createTrustManager();
        if (trustManager != null) {
            setTrustManager(SslUtilities.createSocketFactory(), trustManager);
        }
    }

    public UserRepository provideUserRepoForRegistration() {
        if (userRegisterRepository == null) {
            userRegisterRepository = new UserRepository(getUserRegisterRetrofitInstance()
                    .create(UserRestService.class), rxServiceComposer);
        }
        return userRegisterRepository;
    }

    /**
     * We need a special instance of {@Link DeviceRestService} for the registration call,
     * because we need to send a special header field (Sezame-Hmac)
     *
     * @param hmac_hash
     * @return the repo
     */
    public DeviceRepository provideDeviceRepoForRegistration(String hmac_hash) {
        if (deviceRegisterRepository == null) {
            deviceRegisterRepository = new DeviceRepository(getDeviceRegisterRetrofitInstance(getDeviceRegisterHeaders(hmac_hash))
                    .create(DeviceRestService.class), rxServiceComposer);
        }
        return deviceRegisterRepository;
    }

    public UserRepository provideCommonUserRepo() {
        if (userRepository == null) {
            userRepository = new UserRepository(getCommonRetrofitInstance().create(UserRestService.class), rxServiceComposer);
        }
        return userRepository;
    }

    public DeviceRepository provideCommonDeviceRepo() {
        if (deviceRepository == null) {
            deviceRepository = new DeviceRepository(getCommonRetrofitInstance().create(DeviceRestService.class), rxServiceComposer);
        }
        return deviceRepository;
    }

    public EventRepository provideEventRepo() {
        if (eventRepository == null) {
            eventRepository = new EventRepository(getCommonRetrofitInstance().create(EventRestService.class), rxServiceComposer);
        }
        return eventRepository;
    }

    private Headers getDeviceRegisterHeaders(String hmac_hash) {
        return Headers.of("Content-Type", "application/json; charset=UTF-8",
                "User-Agent", "Sezame",
                "Sezame-Hmac", hmac_hash,
                "Sezame-Hmac-Fields", "User-Agent,Content-Type,Content-Length");
    }

    //endregion PUBLIC

    //region !!!!! RETROFIT // OKHTTP !!!!!!!!

    private Retrofit getUserRegisterRetrofitInstance() {
        if (userRegisterRetrofitInstance == null) {
            userRegisterRetrofitInstance = getRetrofitInstance(regularHeaders);
        }
        return userRegisterRetrofitInstance;
    }

    private Retrofit getDeviceRegisterRetrofitInstance(Headers headers) {
        if (deviceRegisterRetrofitInstance == null) {
            deviceRegisterRetrofitInstance = getRetrofitInstance(headers);
        }
        return deviceRegisterRetrofitInstance;
    }

    private Retrofit getCommonRetrofitInstance() {
        if (commonRetrofitInstance == null) {
            commonRetrofitInstance = getRetrofitInstance(regularHeaders);
        }
        return commonRetrofitInstance;
    }

    private Retrofit getRetrofitInstance(Headers headers) {
        Retrofit.Builder retrofit = new Retrofit.Builder()
                .client(getOkHttpClient(headers))
                .baseUrl(url)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return retrofit.build();
    }

    private OkHttpClient getOkHttpClient(Headers headers) {
        HttpLoggingInterceptor loggingInterceptor
                = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").v(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                /*.addNetworkInterceptor(new StethoInterceptor())*/
                .addInterceptor(loggingInterceptor);

        builder.addInterceptor(chain -> {
            try {
                Request request = chain.request().newBuilder().headers(headers).build();
                return chain.proceed(request);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });

        //Not a user- or device-register-call -> Use certificate
        if (checkIfClientCertificateExists()) {
            builder.sslSocketFactory(socketFactory, trustManager);
        }

        return builder.build();
    }

    private boolean checkIfClientCertificateExists() {
        return socketFactory != null;
    }

    public void setBaseUrl(String url) {
        this.url = url;
    }

    /**
     * ONCE WE HAVE A CERTIFICATE, CALL THIS WITH {@Link KeyStoreUtility#createTrustManager}
     * and {@Link KeyStoreUtility#createTrustManager#createSocketFactory}
     *
     * @param socketFactory
     * @param trustManager
     */
    public void setTrustManager(SSLSocketFactory socketFactory, X509TrustManager trustManager) {
        this.trustManager = trustManager;
        this.socketFactory = socketFactory;
    }

    public void cleanup() {
        trustManager = null;
        socketFactory = null;
        instance = null;
    }

    //endregion
}
