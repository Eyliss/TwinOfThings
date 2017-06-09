package com.twinofthings.models;

import com.google.gson.annotations.SerializedName;

import com.twinofthings.utils.Constants;

/**
 * Created by Eyliss on 6/9/17.
 */

public class Thumbnail {

    @SerializedName(Constants.JSON_FILENAME)
    private String filename;

    @SerializedName(Constants.JSON_CONTENT)
    private String content;

    @SerializedName(Constants.JSON_HASH)
    private String hash;

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }
}
