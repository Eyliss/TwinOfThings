package com.twinofthings.sezamecore.api.dto;

/**
 * @author Michael Präauer
 *         © NOUS 2017
 */

public class ServiceActiveRequestDto {

    private String id;
    private String status;

    public ServiceActiveRequestDto(String id, boolean active) {
        this.id = id;
        this.status = active ? "active" : "inactive";
    }
}
