package com.twinofthings.sezamecore.api.error;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public class ErrorResponse extends Throwable {
    private int code;
    private String message;
    private String data;

    public int getCode() {
        return code;
    }

    @Override public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
