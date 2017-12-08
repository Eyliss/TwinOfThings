package com.twinofthings.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RCSharedPreferences {

    public static final String PRODUCTION_API_BASE_URL = "http://api.wearekabinett.com/api/v1/";
    private static final String KEY_AUTHTOKEN = "authToken";


    private SharedPreferences mPrefs;

    public RCSharedPreferences(Context context) {
        try {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAuthToken(String authToken){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY_AUTHTOKEN, authToken);
        editor.apply();
    }

    public String getAuthToken(){
        return mPrefs.getString(KEY_AUTHTOKEN,"");
    }

}