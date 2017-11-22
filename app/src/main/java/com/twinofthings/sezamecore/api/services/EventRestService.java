package com.twinofthings.sezamecore.api.services;

import com.twinofthings.sezamecore.api.dto.AcknowledgeEventRequestDto;
import com.twinofthings.sezamecore.api.dto.EventResponseDto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public interface EventRestService {
    @GET("/event/")
    Observable<List<EventResponseDto>> getAllEvents();

    @POST("/event")
    Observable<Boolean> acknowledgeEvent(@Body AcknowledgeEventRequestDto dto);
}
