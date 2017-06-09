package com.twinofthings.models;

import com.google.gson.annotations.SerializedName;

import com.twinofthings.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eyliss on 5/19/17.
 */

public class Metadata {

    @SerializedName(Constants.JSON_NAME)
    private String name;

    @SerializedName(Constants.JSON_DESC)
    private String description;

    @SerializedName(Constants.JSON_USER_ID)
    private String userId;

    @SerializedName(Constants.JSON_TIMESTAMP)
    private String timestamp;

    @SerializedName(Constants.JSON_LOCATION)
    private String location;

    @SerializedName(Constants.JSON_THUMBNAIL)
    private Thumbnail thumbnail;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLocation() {
        return location;
    }

}
