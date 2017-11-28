package com.twinofthings.sezamecore;

import com.twinofthings.R;
import com.twinofthings.sezamecore.api.SezameDataProvider;
import com.twinofthings.sezamecore.api.dto.RegisterDeviceRequestDto;
import com.twinofthings.sezamecore.api.dto.RegisterUserRequestDto;
import com.twinofthings.sezamecore.api.dto.RegisterUserResponseDto;
import com.twinofthings.sezamecore.api.error.UserErrorResponse;
import com.twinofthings.sezamecore.api.error.UserRegistrationErrorStatus;
import com.twinofthings.sezamecore.security.KeyStoreUtility;
import com.twinofthings.sezamecore.utils.S;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Felix Tutzer
 *         © Nous 2017
 *         <p>
 *         Providing helper functions to facilitate the registration process
 */
public class SezameRegistrationHelper {

    private static final String TAG = SezameRegistrationHelper.class.getSimpleName();
    private static SezameRegistrationHelper instance;
    private final Resources resources;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private KeyStoreUtility keyStoreUtility;

    public SezameRegistrationHelper(Resources resources) {
        this.resources = resources;
        keyStoreUtility = new KeyStoreUtility();
    }

    public static synchronized SezameRegistrationHelper instance(Resources resources) {
        if (instance == null) {
            instance = new SezameRegistrationHelper(resources);
        }
        return instance;
    }

    public static synchronized SezameRegistrationHelper instance(){
        if (instance == null) {
            Log.d(TAG,"Call instance(Resources resources) first");
        }
        return instance;
    }

    /**
     * Register user and device.
     *
     * @param email     The email address to be registered
     * @param pushToken Firebase push token
     * @return Observable -> true: Successfully registered, false: In recovery mode,
     * error: something went wrong.
     */
    public Observable<Boolean> register(String email, String pushToken) {
        PublishSubject<Boolean> result = PublishSubject.create();
        disposables.add(SezameDataProvider.instance().provideUserRepoForRegistration()
                .register(createDto(email, resources.getString(R.string.language))).subscribe(response -> {
                    Log.d("User Id %s", response.getUsername());
                  Log.d("Device Id %s", response.getDevice_id());
                  Log.d("Shared Secret %s", response.getReg_shared_secret());

                    registerDevice(pushToken, response, result);

                }, getRegisterThrowable(result)));
        return result;
    }

    @NonNull
    /**
     * If user registration fails
     */
    private Consumer<Throwable> getRegisterThrowable(PublishSubject<Boolean> result) {
        return throwable -> {
            if (throwable instanceof UserErrorResponse) {
                switch (UserRegistrationErrorStatus.getStatus((UserErrorResponse) throwable)) {
                    case INCOMPLETE_USER_REGISTRATION:
                        //Recovery -> User is brought to the QR scanner
                        result.onNext(false);
                        break;
                    case USER_ACTIVE:
                        result.onError(new Throwable(resources.getString(R.string.error_user_active)));
                        break;
                    case DEVICE_INACTIVE:
                        result.onError(new Throwable(resources.getString(R.string.error_device_inactive)));
                        break;
                    case DEVICE_DELETED:
                        //Recovery -> User is brought to the QR scanner
                        result.onNext(false);
                        break;
                    case USER_IN_RECOVERY:
                        //Recovery -> User is brought to the QR scanner
                        result.onNext(false);
                        break;
                    case INTERNAL_SERVER_ERROR:
                        result.onError(new Throwable(resources.getString(R.string.error_internal)));
                        break;
                    default:
                        result.onError(new Throwable(resources.getString(R.string.error_general)));

                }
            } else {
                result.onError(new Throwable(resources.getString(R.string.error_general)));
                Log.e(TAG,throwable.toString());
            }
        };
    }

    /**
     * Register device in recovery mode (after device has been deleted)
     *
     * @param pushToken Firebase push token
     * @param response  The data fetched from the recovery QR code
     * @return Observable -> true: Device recovery successful, error: something went wrong.
     */
    public Observable<Boolean> deviceRecovery(String pushToken, RegisterUserResponseDto response) {
        PublishSubject<Boolean> result = PublishSubject.create();

        registerDevice(pushToken, response, result);

        return result;
    }

    private void registerDevice(String pushToken, RegisterUserResponseDto response, PublishSubject<Boolean> result) {

        final RegisterDeviceRequestDto dto = createDeviceRegistrationDto(pushToken, response);
        final String hmac_header = keyStoreUtility.createHmacHashForDeviceRegisterCall(response.getReg_shared_secret(), dto);
        disposables.add(SezameDataProvider.instance().provideDeviceRepoForRegistration(hmac_header).registerDevice(dto).subscribe(r -> {
            if (S.isBlank(r.getCert())) {
                Log.e(TAG,"Error while registering certificate, certificate is empty");
                result.onError(new Throwable("Invalid certificate exchange"));
            } else {
                try {
                    keyStoreUtility.storeCertificateInKeystore(r.getCert());
                    //Device register call successful
                    result.onNext(true);
                } catch (Exception e) {
                    result.onError(new Throwable("Invalid certificate exchange"));
                    Log.e(TAG, "Error while registering certificate");
                }
            }
        }, throwable -> {
            result.onError(new Throwable("An error occurred: " + throwable.getMessage()));
            Log.e(TAG,throwable.toString());
        }));
    }

    private RegisterDeviceRequestDto createDeviceRegistrationDto(String pushToken, RegisterUserResponseDto response) {
        final String certificateRequestString = keyStoreUtility.createKeyPairAndReturnCertificateRequest(response.getUsername(),
                response.getDevice_id());
        return new RegisterDeviceRequestDto.Builder()
                .device_id(response.getDevice_id())
                .push(pushToken)
                .username(response.getUsername())
                .csr(certificateRequestString).build();
    }

    private RegisterUserRequestDto createDto(String email, String language) {
        return new RegisterUserRequestDto.Builder()
                .email(email)
                .lang(language)
                .build();
    }

    /**
     * dispose all disposables to avoid leaks
     */
    public void cleanUp() {
        disposables.clear();
    }
}
