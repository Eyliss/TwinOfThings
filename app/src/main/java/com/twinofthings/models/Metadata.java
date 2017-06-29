package com.twinofthings.models;

import com.google.gson.annotations.SerializedName;

import com.twinofthings.utils.Constants;

/**
 * Created by Eyliss on 5/19/17.
 */

public class Metadata {

    @SerializedName(Constants.JSON_BRAND_NAME)
    private String brandName;

    @SerializedName(Constants.JSON_PRODUCT_NAME)
    private String productName;

    @SerializedName(Constants.JSON_PRODUCT_SUBLINE)
    private String productSubline;

    @SerializedName(Constants.JSON_TIMESTAMP)
    private String timestamp;

    @SerializedName(Constants.JSON_OWNER_NAME)
    private String ownerName;

    @SerializedName(Constants.JSON_SERIAL_ID)
    private String serialId;

    @SerializedName(Constants.JSON_MATERIAL)
    private String material;

    @SerializedName(Constants.JSON_COMMENTS_DETAIL)
    private String commentsDetail;

    @SerializedName(Constants.JSON_THUMBNAIL)
    private Thumbnail thumbnail;


    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSubline() {
        return productSubline;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getSerialId() {
        return serialId;
    }

    public String getMaterial() {
        return material;
    }

    public String getCommentsDetail() {
        return commentsDetail;
    }
}
