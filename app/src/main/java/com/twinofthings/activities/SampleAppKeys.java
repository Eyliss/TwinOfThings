package com.twinofthings.activities;

/**
 * Keys used by the Sample Application are declared here.
 * Created by nxp69547 on 8/3/2016.
 */
public final class SampleAppKeys {

    /**
     * Private constructor restricts Implementation.
     */
    private SampleAppKeys(){

    }



    /**
     * Only these types of Keys can be stored by the Helper class.
     */
    public static enum EnumKeyType{
        EnumAESKey,
        EnumDESKey,
        EnumMifareKey
    }

    /**
     * Default key with Value FF.
     */
    public static final byte[] KEY_DEFAULT_FF = {(byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    /**
     * 16 bytes AES128 Key.
     */
    public static final byte[] KEY_AES128 = {(byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    /**
     * 16 bytes AES128 Key.
     */
    public static final byte[] KEY_AES128_ZEROS = {(byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    /**
     * 24 bytes 2KTDES Key.

    public static final byte[] KEY_2KTDES = {(byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
     */

    public static final byte[] KEY_2KTDES ={
            (byte) 0xAA, (byte) 0x08, (byte) 0x57, (byte) 0x92, (byte) 0x1C,
            (byte) 0x76, (byte) 0xFF, (byte) 0x65, (byte) 0xE7, (byte) 0xD2,
            (byte) 0x78, (byte) 0x44, (byte) 0xF8, (byte) 0x0F, (byte) 0x8D,
            (byte) 0x1B, (byte) 0xE7, (byte) 0xC2, (byte) 0xF0, (byte) 0x89,
            (byte) 0x04, (byte) 0xC0, (byte) 0xC3, (byte) 0xE3 };


    /**
     * 16 bytes 2KTDES_ULC Key.
     */
    public static final byte[] KEY_2KTDES_ULC = {(byte) 0x49, (byte) 0x45,
            (byte) 0x4D, (byte) 0x4B, (byte) 0x41, (byte) 0x45, (byte) 0x52,
            (byte) 0x42, (byte) 0x21, (byte) 0x4E, (byte) 0x41, (byte) 0x43,
            (byte) 0x55, (byte) 0x4F, (byte) 0x59, (byte) 0x46};
}

