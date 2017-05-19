package com.twinofthings.models;


import com.google.gson.annotations.SerializedName;

import com.twinofthings.utils.Constants;

public class Transaction {

    @SerializedName(Constants.JSON_ID)
    private int id;

    @SerializedName(Constants.JSON_METADATA)
    private Metadata metadata;

    public int getId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }
}