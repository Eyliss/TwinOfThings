package com.twinofthings.api;

import com.google.gson.Gson;

import com.twinofthings.R;

public class RCApiResponse<T> {

    private String status;
    private Object data;
    private String message = "";
    private String error = "";

    public String getStatus(){
        return status;
    }

    public Object getData(){
        return data;
    }

    public String getStringData(){
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public String getMessage(){
        return message;
    }

    public String getError(){
        return error;
    }

    public boolean notFoundError(){
        return error.equals(R.string.transaction_error);
    }

    public boolean isSuccessful(){
        return Boolean.parseBoolean(status);
    }

}
