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

    public static void validate(String publicKey, String signature, String challenge, Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        object.addProperty("public_key",publicKey);
        object.addProperty("signature",signature);
        object.addProperty("challenge",challenge);

        Call<RCApiResponse> call = RCServiceWithAuth.validate(object);
        call.enqueue(callback);
    }

    public static void validateTransaction(String publicKey, Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        object.addProperty("public_key",publicKey);

        Call<RCApiResponse> call = RCServiceWithAuth.validate(object);
        call.enqueue(callback);
    }

    public static void provision(String publicKey, String signature, String challenge, String name, String desc, String id, String timestamp, String location, Callback<RCApiResponse> callback){
        JsonObject object = new JsonObject();
        object.addProperty("public_key",publicKey);
        object.addProperty("signature",signature);
        object.addProperty("challenge",challenge);

        JsonObject metadata = new JsonObject();
        metadata.addProperty("name",name);
        metadata.addProperty("short_desc",desc);
        metadata.addProperty("user_id",id);
        metadata.addProperty("timestamp",timestamp);
        metadata.addProperty("location",location);
        object.add("metadata",metadata);

        Call<RCApiResponse> call = RCServiceWithAuth.provision(object);
        call.enqueue(callback);
    }

}
