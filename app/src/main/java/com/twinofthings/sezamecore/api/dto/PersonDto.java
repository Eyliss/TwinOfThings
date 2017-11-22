package com.twinofthings.sezamecore.api.dto;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public final class PersonDto {
    private String email;
    private String securityQuestion1;
    private String securityQuestion2;
    private String securityAnswer1;
    private String securityAnswer2;
    private String language;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSecurityQuestion1(String securityQuestion1) {
        this.securityQuestion1 = securityQuestion1;
    }

    public void setSecurityQuestion2(String securityQuestion2) {
        this.securityQuestion2 = securityQuestion2;
    }

    public void setSecurityAnswer1(String securityAnswer1) {
        this.securityAnswer1 = securityAnswer1;
    }

    public void setSecurityAnswer2(String securityAnswer2) {
        this.securityAnswer2 = securityAnswer2;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
