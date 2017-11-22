package com.twinofthings.sezamecore.api.dto;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public final class RegisterDeviceResponseDto {
    private String cert;

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }
}
