package com.twinofthings.sezamecore.api.dto;

import com.squareup.moshi.Json;

/**
 * @author Michael Präauer
 *         © NOUS 2017
 */

public class ChangeEmailRequestDto {

    private String old;

    @Json(name = "new")
    private String newEmailAddress;

    public ChangeEmailRequestDto(String old, String newEmailAddress) {
        this.old = old;
        this.newEmailAddress = newEmailAddress;
    }
}
