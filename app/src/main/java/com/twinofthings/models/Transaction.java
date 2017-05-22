package com.twinofthings.models;


import com.google.gson.annotations.SerializedName;

import com.twinofthings.utils.Constants;

public class Transaction {

    @SerializedName(Constants.JSON_ID)
    private String id;

    @SerializedName(Constants.JSON_METADATA)
    private Metadata metadata;

    public String getId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }
}