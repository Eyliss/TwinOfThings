package com.twinofthings.sezamecore.api.dto;

/**
 * @author Michael Präauer
 *         © NOUS 2017
 */

public class EventResponseDto {
    private String id;
    private String type;    //'auth', 'registerclient', 'removedevice', 'fraud'
    private String status;
    private String error;
    private String message;
    private String validuntil;
    private String client_id;
    private String user_id;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getClient_id() {
        return client_id;
    }
}
