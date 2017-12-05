package com.twinofthings.models;

import com.google.gson.annotations.SerializedName;

import com.twinofthings.utils.Constants;

import android.os.Parcel;
import android.os.Parcelable;

public class Credentials implements Parcelable {

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

    protected Credentials(Parcel in) {
        publicKey = in.readString();
        challenge = in.readString();
        signature = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(publicKey);
        dest.writeString(challenge);
        dest.writeString(signature);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Credentials> CREATOR = new Parcelable.Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel in) {
            return new Credentials(in);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };
}
