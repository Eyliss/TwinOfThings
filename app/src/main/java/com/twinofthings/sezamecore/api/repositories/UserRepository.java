package com.twinofthings.sezamecore.api.repositories;

import com.twinofthings.sezamecore.api.RxServiceComposer;
import com.twinofthings.sezamecore.api.dto.AddPairingRequestDto;
import com.twinofthings.sezamecore.api.dto.ChangeEmailRequestDto;
import com.twinofthings.sezamecore.api.dto.RegisterUserRequestDto;
import com.twinofthings.sezamecore.api.dto.RegisterUserResponseDto;
import com.twinofthings.sezamecore.api.dto.ServiceActiveRequestDto;
import com.twinofthings.sezamecore.api.dto.UserPairingsResponseDto;
import com.twinofthings.sezamecore.api.error.UserErrorResponse;
import com.twinofthings.sezamecore.api.services.UserRestService;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Felix Tutzer
 *         © Nous 2017
 */

public class UserRepository {
    private UserRestService service;
    private RxServiceComposer composer;

    public UserRepository(UserRestService service, RxServiceComposer composer) {
        this.service = service;
        this.composer = composer;
    }

    public Observable<RegisterUserResponseDto> register(RegisterUserRequestDto dto) {
        return service.register(dto).compose(composer.apiRequestTransformerIO(UserErrorResponse.class));
    }

    public Observable<Boolean> addPairing(AddPairingRequestDto dto) {
        return service.addPairing(dto).compose(composer.apiRequestTransformer());
    }

    public Observable<List<UserPairingsResponseDto>> getPairings() {
        return service.getPairings().compose(composer.apiRequestTransformer());
    }

    public Observable<Boolean> deletePairing(String serviceId) {
        return service.deletePairing(serviceId).compose(composer.apiRequestTransformer());
    }

    public Observable<Boolean> changeEmail(ChangeEmailRequestDto dto) {
        return service.changeEmail(dto).compose(composer.apiRequestTransformer());
    }

    public Observable<Boolean> resendEmail() {
        return service.resendEmail().compose(composer.apiRequestTransformer());
    }

    public Observable<Boolean> deleteAccount() {
        return service.deleteAccount().compose(composer.apiRequestTransformer());
    }

    public Observable<Boolean> setServiceActive(ServiceActiveRequestDto dto) {
        return service.setServiceActive(dto).compose(composer.apiRequestTransformer());
    }

}
