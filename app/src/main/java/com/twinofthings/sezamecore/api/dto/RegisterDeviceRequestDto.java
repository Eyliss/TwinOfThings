package com.twinofthings.sezamecore.api.dto;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public final class RegisterDeviceRequestDto {
    private PushDto push;
    private String csr;
    private String device_id;
    //    private String username;
    private String public_key;

    public static final class PushDto {
        private String type;
        private String token;

        public String getType() {
            return type;
        }

        public String getToken() {
            return token;
        }

        public PushDto(String token) {
            this.type = "firebase";
            this.token = token;
        }
    }

    public static class Builder {
        private RegisterDeviceRequestDto dto = new RegisterDeviceRequestDto();

        public Builder push(String token) {
            dto.push = new PushDto(token);
            return this;
        }

        public Builder csr(String csr) {
            this.dto.csr = csr;
            return this;
        }

        public Builder device_id(String device_id) {
            this.dto.device_id = device_id;
            return this;
        }

        public Builder username(String username) {
//            this.dto.username = username;
            return this;
        }

        public Builder publicKey(String pk) {
            this.dto.public_key = pk;
            return this;
        }

        public RegisterDeviceRequestDto build() {
            return dto;
        }

    }
}
