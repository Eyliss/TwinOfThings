package com.twinofthings.utils;

/**
 * Class where it contains all common functions for the project
 */
public class Util {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    //Converts a bytes array into a hexadecimal string
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}