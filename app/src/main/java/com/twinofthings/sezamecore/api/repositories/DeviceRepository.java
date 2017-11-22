package com.twinofthings.sezamecore.api.repositories;



import com.twinofthings.sezamecore.api.RxServiceComposer;
import com.twinofthings.sezamecore.api.dto.RegisterDeviceRequestDto;
import com.twinofthings.sezamecore.api.dto.RegisterDeviceResponseDto;
import com.twinofthings.sezamecore.api.error.ErrorResponse;
import com.twinofthings.sezamecore.api.services.DeviceRestService;

import io.reactivex.Observable;

/**
 * @author Felix Tutzer
 *         © Nous 2017
 */

public class DeviceRepository {
    private DeviceRestService service;
    private RxServiceComposer composer;

    public DeviceRepository(DeviceRestService service, RxServiceComposer composer) {
        this.service = service;
        this.composer = composer;
    }

    public Observable<RegisterDeviceResponseDto> registerDevice(RegisterDeviceRequestDto dto) {
        return service.register(dto).compose(composer.apiRequestTransformerIO(ErrorResponse.class));
    }

    public Observable<Boolean> deleteDevice() {
        return service.deleteDevice().compose(composer.apiRequestTransformer());
    }
}
