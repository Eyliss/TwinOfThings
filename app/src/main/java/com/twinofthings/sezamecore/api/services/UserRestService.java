package com.twinofthings.sezamecore.api.services;

import com.twinofthings.sezamecore.api.dto.AddPairingRequestDto;
import com.twinofthings.sezamecore.api.dto.ChangeEmailRequestDto;
import com.twinofthings.sezamecore.api.dto.RegisterUserRequestDto;
import com.twinofthings.sezamecore.api.dto.RegisterUserResponseDto;
import com.twinofthings.sezamecore.api.dto.ServiceActiveRequestDto;
import com.twinofthings.sezamecore.api.dto.UserPairingsResponseDto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public interface UserRestService {
    @POST("/user/register")
    Observable<RegisterUserResponseDto> register(@Body RegisterUserRequestDto dto);

    @PUT("/user/exists")
    Observable<Object> exists();

    @POST("/user/link/")
    Observable<Boolean> addPairing(@Body AddPairingRequestDto dto);

    @GET("/user/link/")
    Observable<List<UserPairingsResponseDto>> getPairings();

    @DELETE("/user/link/{serviceId}")
    Observable<Boolean> deletePairing(@Path("serviceId") String serviceId);

    @DELETE("/user/")
    Observable<Boolean> deleteAccount();

    @POST("/user/email")
    Observable<Boolean> changeEmail(@Body ChangeEmailRequestDto dto);

    @GET("/user/email")
    Observable<Boolean> resendEmail();

    @GET("/user/email/safe")
    Observable<Boolean> resendSezameSafeEmail();

    @PUT("/user/link/")
    Observable<Boolean> setServiceActive(@Body ServiceActiveRequestDto dto);
}
