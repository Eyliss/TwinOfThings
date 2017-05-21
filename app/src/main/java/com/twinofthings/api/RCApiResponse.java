package com.twinofthings.api;

public class RCApiResponse<T> {

    private String status;
    private Object data;
    private String message = "";


    public String getStatus(){
        return status;
    }

    public Object getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isSuccessful(){
        return Boolean.parseBoolean(status);
    }

}
