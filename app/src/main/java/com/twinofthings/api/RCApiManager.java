package com.twinofthings.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;

import static com.twinofthings.activities.ReaderActivity.TAG;

public class RCApiManager {

    static RCApiService RCService = ServiceGenerator.createService(RCApiService.class,false);
    static RCApiService RCServiceWithAuth = ServiceGenerator.createService(RCApiService.class,true);

    public static void getRng(Callback<RCApiResponse> callback){
        Call<RCApiResponse> call = RCService.getRng();
        call.enqueue(callback);
    }

    public static void getPublicKey(Callback<RCApiResponse> callback){
        Call<RCApiResponse> call = RCService.getPublicKey();
        call.enqueue(callback);
    }

    public static void getCredentials(Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        Call<RCApiResponse> call = RCService.getCredentials(object);
        call.enqueue(callback);
    }

    public static void sendHashMessage(String message, Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        object.addProperty("s",message);
        Call<RCApiResponse> call = RCService.sendHash(object);
        call.enqueue(callback);
    }

    public static void sendSignature(String signature, Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        object.addProperty("s",signature);
        Call<RCApiResponse> call = RCService.sendSignature(object);
        call.enqueue(callback);
    }

    public static void validate(String publicKey, String signature, String challenge, String sezamePk, String sezameSign, Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        object.addProperty("public_key",publicKey);
        object.addProperty("signature",signature);
        object.addProperty("challenge",challenge);
        object.addProperty("sezame_pk",sezamePk);
        object.addProperty("sezame_sig",sezameSign);

        Call<RCApiResponse> call = RCServiceWithAuth.validate(object);
        call.enqueue(callback);
    }

    public static void validateTransaction(String publicKey, Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        object.addProperty("public_key",publicKey);

        Call<RCApiResponse> call = RCServiceWithAuth.validate(object);
        call.enqueue(callback);
    }

    public static void provision(String publicKey, String signature, String challenge, String sezamePk, String sezameSign, String brandName, String productName, String productSubline, String timestamp, String ownerName, String serialId, String material, String commentsDetail, String thumb, Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        object.addProperty("public_key",publicKey);
        object.addProperty("signature",signature);
        object.addProperty("challenge",challenge);
        object.addProperty("sezame_pk",sezamePk);
        object.addProperty("sezame_sig",sezameSign);

        JsonObject metadata = new JsonObject();
        metadata.addProperty("brand_name",brandName);
        metadata.addProperty("product_name",productName);
        metadata.addProperty("product_subline",productSubline);
        metadata.addProperty("timestamp",timestamp);
        metadata.addProperty("owner_name",ownerName);
        metadata.addProperty("serial_id",serialId);
        metadata.addProperty("material",material);
        metadata.addProperty("comments_detail",commentsDetail);

        JsonObject thumbnail = new JsonObject();
        thumbnail.addProperty("filename",System.currentTimeMillis()+".png");
        thumbnail.addProperty("content",thumb);
        metadata.add("thumbnail",thumbnail);

        object.add("metadata",metadata);

        Call<RCApiResponse> call = RCServiceWithAuth.provision(object);
        call.enqueue(callback);
    }

}
