package com.twinofthings.sezamecore.utils;

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;

import java.io.IOException;

/**
 * @author Felix Tutzer
 *         © Nous 2017
 */

public class S {
    public static final String EMPTY = "";

    private S() {
        // No instances.
    }

    public static boolean isBlank(CharSequence string) {
        return (string == null || string.toString().trim().length() == 0);
    }

    public static String valueOrDefault(String string, String defaultString) {
        return isBlank(string) ? defaultString : string;
    }

    public static String toJsonString(Object obj, Class c) {
        final Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(c).toJson(obj);
    }

    public static <T> T deserialize(String serialized, Class c) {
        final Moshi moshi = new Moshi.Builder().build();
        try {
            return (T) moshi.adapter(c).fromJson(serialized);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonDataException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean equals(String a, String b) {
        if (a == null && b == null) return true;
        return a != null && a.equals(b);
    }
}
