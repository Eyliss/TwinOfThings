package com.twinofthings.sezamecore.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * @author Felix Tutzer
 *         © Nous 2017
 *         <p>
 *         SHARED PREFERENCE UTIL CLASS
 */

public class P {
    private final SharedPreferences prefs;
    private static P instance;
    public static final String SHARED_PREFS_KEY = "SEZAME_PREFS";
    public static final String BASE_URL = "BASE_URL";

    private P(Context context) {
        this.prefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }

    public static synchronized void init(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        if (instance == null) {
            instance = new P(context);
        }
    }

    public synchronized static P instance() {
        if (instance == null) {
            throw new IllegalArgumentException("Call init(Context) before instance()");
        }
        return instance;
    }

    private SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

    public void putString(@NonNull String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public void removeString(@NonNull String key) {
        getEditor().remove(key);
    }

    public String getString(@NonNull String key) {
        return prefs.getString(key, "");
    }

    public boolean getBoolean(@NonNull String key) {
        return prefs.getBoolean(key, false);
    }

    /**
     * Remove all stored values from the Shared Preferences
     */
    public void clear() {
        getEditor().clear().apply();
    }
}
