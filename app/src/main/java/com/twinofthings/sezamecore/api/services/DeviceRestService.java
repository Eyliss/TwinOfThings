package com.twinofthings.sezamecore.api.services;

import com.twinofthings.sezamecore.api.dto.RegisterDeviceRequestDto;
import com.twinofthings.sezamecore.api.dto.RegisterDeviceResponseDto;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public interface DeviceRestService {
    @POST("/device/register")
    Observable<RegisterDeviceResponseDto> register(@Body RegisterDeviceRequestDto dto);

    @POST("/device/remove")
    Observable<Boolean> deleteDevice();
}
