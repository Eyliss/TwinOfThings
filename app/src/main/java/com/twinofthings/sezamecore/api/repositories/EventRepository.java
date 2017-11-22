package com.twinofthings.sezamecore.api.repositories;

import com.twinofthings.sezamecore.api.RxServiceComposer;
import com.twinofthings.sezamecore.api.dto.AcknowledgeEventRequestDto;
import com.twinofthings.sezamecore.api.dto.EventResponseDto;
import com.twinofthings.sezamecore.api.services.EventRestService;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Michael Präauer
 *         © Nous 2017
 */

public class EventRepository {
    private EventRestService service;
    private RxServiceComposer composer;

    public EventRepository(EventRestService service, RxServiceComposer composer) {
        this.service = service;
        this.composer = composer;
    }

    public Observable<List<EventResponseDto>> getAllEvents() {
        return service.getAllEvents().compose(composer.apiRequestTransformer());
    }

    public Observable<Boolean> acknowledgeEvent(AcknowledgeEventRequestDto dto) {
        return service.acknowledgeEvent(dto).compose(composer.apiRequestTransformer());
    }
}
