package com.twinofthings.sezamecore.api.dto;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public final class RegisterUserRequestDto {
    private PersonDto person;

    public static final class Builder {
        RegisterUserRequestDto dto = new RegisterUserRequestDto();
        PersonDto personDto = new PersonDto();

        public Builder email(String email) {
            personDto.setEmail(email);
            return this;
        }

        public Builder lang(String lang) {
            personDto.setLanguage(lang);
            return this;
        }

        public RegisterUserRequestDto build() {
            dto.person = personDto;
            return dto;
        }
    }
}
