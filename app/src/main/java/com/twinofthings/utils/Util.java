package com.twinofthings.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Class where it contains all common functions for the project
 */
public class Util {

    public static final int MAX_IMAGE_LENGTH = 1 * 1024 * 1024;


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

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                  + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Encode a bitmap image decreasing its quality with several compress
     * until stream length is less than MAX_FANSTASTIC_IMAGE_LENGTH
     *
     * @param image bitmap to encode
     * @param format in which we want to compress the image (.PNG, .JPEG, etc)
     * @return string with the 16-byte hash value encrypted
     */
    public static String encodeBitmapToBase64(Bitmap image, Bitmap.CompressFormat format)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int compressQuality = 104; // quality decreasing by 5 every loop. (start from 99)
        int streamLength = MAX_IMAGE_LENGTH;
        while (streamLength >= MAX_IMAGE_LENGTH) {
            outputStream = new ByteArrayOutputStream();
            compressQuality -= 5;
            image.compress(format, compressQuality, outputStream);
            byte[] bmpPicByteArray = outputStream.toByteArray();
            streamLength = bmpPicByteArray.length;
        }
        byte[] b = outputStream.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64toBitmap(String image){
        byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedBitmap;
    }
}