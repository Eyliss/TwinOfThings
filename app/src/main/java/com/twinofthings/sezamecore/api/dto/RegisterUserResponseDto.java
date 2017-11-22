package com.twinofthings.sezamecore.api.dto;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public final class RegisterUserResponseDto {
    private String username;
    private String device_id;
    private String reg_shared_secret;

    public String getUsername() {
        return username;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getReg_shared_secret() {
        return reg_shared_secret;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDeviceId(String deviceId) {
        this.device_id = deviceId;
    }

    public void setSharedSecret(String sharedSecret) {
        this.reg_shared_secret = sharedSecret;
    }
}
