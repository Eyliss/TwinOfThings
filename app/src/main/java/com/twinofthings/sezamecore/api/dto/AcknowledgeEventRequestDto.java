package com.twinofthings.sezamecore.api.dto;

/**
 * @author Michael Präauer
 *         © NOUS 2017
 */

public class AcknowledgeEventRequestDto {

    private String transaction_id;
    private String status;

    public AcknowledgeEventRequestDto(String transaction_id, AcknowledgeStatus status) {
        this.transaction_id = transaction_id;
        this.status = status.name();
    }
}
