package com.twinofthings.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Eyliss on 12/1/17.
 */

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

    public void setGcmToken(String gcm){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY_GCM, gcm);
        editor.apply();
    }

    public String getGcmToken(){
        return mPrefs.getString(KEY_GCM,"");
    }

    public void setNotificationsTimestamp(long millis){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(KEY_NOTIFICATIONS_TIMESTAMP, millis);
        editor.apply();
    }

    public long getNotificationsTimestamp(){
        return mPrefs.getLong(KEY_NOTIFICATIONS_TIMESTAMP,0L);
    }

    public void changeEnv(){
        SharedPreferences.Editor editor = mPrefs.edit();

        if(!isLive()){
            editor.putString(KEY_ENV, PRODUCTION_API_BASE_URL);
            editor.putBoolean(KEY_IS_LIVE, true);

        }else {
            editor.putString(KEY_ENV, DEV_API_BASE_URL);
            editor.putBoolean(KEY_IS_LIVE, false);

        }
        editor.apply();

    }

    public String getEnvironment(){
        return mPrefs.getString(KEY_ENV,PRODUCTION_API_BASE_URL);
    }

    public boolean isLive(){
        return mPrefs.getBoolean(KEY_IS_LIVE, true);
    }

    public boolean wizardDisabled(){
        return mPrefs.getBoolean(KEY_WIZARD, false);
    }
    public void disableWizard(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_WIZARD, true);
        editor.apply();
    }

    public boolean videoPopupDisabled(){
        return mPrefs.getBoolean(KEY_POPUP_VIDEOS, false);
    }
    public void disableVideoPopup(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_POPUP_VIDEOS, true);
        editor.apply();
    }

    public boolean journalPopupDisabled(){
        return mPrefs.getBoolean(KEY_POPUP_JOURNAL, false);
    }
    public void disableJournalPopup(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_POPUP_JOURNAL, true);
        editor.apply();
    }

}