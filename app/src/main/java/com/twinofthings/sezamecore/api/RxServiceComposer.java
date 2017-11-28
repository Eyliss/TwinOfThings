package com.twinofthings.sezamecore.api;

import com.twinofthings.sezamecore.api.error.ErrorResponse;
import com.twinofthings.sezamecore.schedulers.BaseSchedulerProvider;
import com.twinofthings.sezamecore.utils.S;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @author Felix Tutzer
 *         © Nous 2017
 *         <p>
 *         Provides functionality to compose api call streams
 *         1) adds schedulers
 *         2) adds error handling with custom error-response of type {@Link ErrorResponse}
 */

public class RxServiceComposer {

    private final String TAG = RxServiceComposer.class.getSimpleName();
    private final BaseSchedulerProvider schedulerProvider;

    public RxServiceComposer(BaseSchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Adds io and ui schedulers + parses api error response to a {@Link ErrorResponse} object
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> apiRequestTransformer() {
        return tObservable -> tObservable
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .compose(apiErrorTransformer(ErrorResponse.class));
    }

    public <T> ObservableTransformer<T, T> apiRequestTransformerIO(Class errorResponseType) {
        return tObservable -> tObservable
                .subscribeOn(schedulerProvider.io())
                .compose(apiErrorTransformer(errorResponseType));
    }

    public <T> ObservableTransformer<T, T> apiErrorTransformer(Class errorResponseType) {

        return observable -> observable.onErrorResumeNext(new Function<Throwable, ObservableSource<? extends T>>() {
            @Override
            public Observable<? extends T> apply(Throwable throwable) {
                if (throwable instanceof HttpException) {
                    ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream()));

                    String line;
                    StringBuilder sb = new StringBuilder();
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        Log.e(TAG,e.getMessage());
                    }
                    if (!S.isBlank(sb)) {
                        Log.e(TAG,"Request error-response: "+ sb.toString());
                        try {
                            ErrorResponse response = S.deserialize(sb.toString(), errorResponseType);
                            response.setCode(((HttpException) throwable).response().code());
                            return Observable.error(response);
                        } catch (Exception e) {
                            ErrorResponse response = S.deserialize(sb.toString(), ErrorResponse.class);
                            if (response != null) {
                                response.setCode(((HttpException) throwable).response().code());
                                return Observable.error(response);
                            }
                        }
                    }
                    return Observable.error(throwable);
                }
                // if not the kind we're interested in, then just report the same error to onError()
                return Observable.error(throwable);
            }
        });
    }

}
