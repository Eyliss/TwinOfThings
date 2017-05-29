package com.twinofthings.models;

import com.google.gson.annotations.SerializedName;

import com.twinofthings.utils.Constants;

public class Credentials {

    @SerializedName(Constants.PUB_KEY)
    private String publicKey;

    @SerializedName(Constants.CHALLENGE)
    private String challenge;

    @SerializedName(Constants.SIGNATURE)
    private String signature;

    public String getPublicKey() {
        return publicKey;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getSignature() {
        return signature;
    }
}
