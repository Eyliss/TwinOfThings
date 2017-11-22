package com.twinofthings.sezamecore.api.error;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public class UserErrorResponse extends ErrorResponse {

    private boolean device;
    private String status;

    public boolean isDevice() {
        return device;
    }

    public String getStatus() {
        return status;
    }
}
