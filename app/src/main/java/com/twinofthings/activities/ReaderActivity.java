package com.twinofthings.activities;

import com.google.gson.Gson;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.CustomModules;
import com.nxp.nfclib.KeyType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.classic.ClassicFactory;
import com.nxp.nfclib.classic.IMFClassic;
import com.nxp.nfclib.classic.IMFClassicEV1;
import com.nxp.nfclib.defaultimpl.KeyData;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.DESFireFile;
import com.nxp.nfclib.desfire.EV1ApplicationKeySettings;
import com.nxp.nfclib.desfire.IDESFireEV1;
import com.nxp.nfclib.desfire.IDESFireEV2;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.interfaces.IKeyData;
import com.nxp.nfclib.ndef.NdefMessageWrapper;
import com.nxp.nfclib.ndef.NdefRecordWrapper;
import com.nxp.nfclib.utils.NxpLogUtils;
import com.nxp.nfclib.utils.Utilities;
import com.twinofthings.R;
import com.twinofthings.api.RCApiManager;
import com.twinofthings.api.RCApiResponse;
import com.twinofthings.fragments.AlertDialogFragment;
import com.twinofthings.fragments.ProvisioningFragment;
import com.twinofthings.fragments.ScanFragment;
import com.twinofthings.helpers.KeyInfoProvider;
import com.twinofthings.helpers.SampleAppKeys;
import com.twinofthings.models.Credentials;
import com.twinofthings.utils.Constants;
import com.twinofthings.utils.Util;

import java.io.File;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReaderActivity extends AppCompatActivity {

    public static final String TAG = ReaderActivity.class.getSimpleName();
    private static final String SCAN_FRAGMENT_TAG = "scan_fragment_tag";
    private static final String CREATE_FRAGMENT_TAG = "create_twin_fragment_tag";

    private Fragment mFragment;

    private String publicKey = "AA8BC774646ADF5C9B753652379DE877C70087EB711A351580CC7261738BB65ABE33E1E370190EE74FD79421C8C4F80F9375CE2E687CC5D155C453CB33876CF7";
    private String signature = "1948D64C603E29035A7926F7B3CD3235AECD1A39816E74932287814EEA52FE27E2CC4EC4171C94C372870B8D4FF43337AD12D01EF7CFD52C7B432869576CAD97";
    private String challenge = "9834876DCFB05CB167A5C24953EBA58C4AC89B1ADF57F28F2F9D09AF107EE8F0";
    private String tagId = "";

    private IKeyData objKEY_2KTDES_ULC = null;
    private IKeyData objKEY_2KTDES = null;
    private IKeyData objKEY_AES128 = null;
    private byte[] default_ff_key = null;
    private IKeyData default_zeroes_key = null;


    private static final String ALIAS_KEY_AES128 = "key_aes_128";

    private static final String ALIAS_KEY_2KTDES = "key_2ktdes";

    private static final String ALIAS_KEY_2KTDES_ULC = "key_2ktdes_ulc";

    private static final String ALIAS_DEFAULT_FF = "alias_default_ff";

    private static final String ALIAS_KEY_AES128_ZEROES = "alias_default_00";

    private static final String EXTRA_KEYS_STORED_FLAG = "keys_stored_flag";


    /**
     * NDEF MESSAGE DATA !!
     */

    static String ndefData = "Mifare";

    static String ndefDataslix2 = "MifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeam" +
            "MifareSDKTeamMifar";

    /**
     * Package Key.
     */
    static String packageKey = "028e3bbffafd128ff6a88c6d351e2283";

    /**
     * KEY_APP_MASTER key used for encrypt data.
     */
    private static final String KEY_APP_MASTER = "This is my key  ";
    /**
     * NxpNfclib instance.
     */
    private NxpNfcLib libInstance = null;
    /**
     * bytes key.
     */
    private byte[] bytesKey = null;
    /**
     * Cipher instance.
     */
    private Cipher cipher = null;
    /**
     * Iv.
     */
    private IvParameterSpec iv = null;
    /**
     * text view instance.
     */
    private TextView tv = null;
    /**
     * Image view inastance.
     */
    private ImageView mImageView = null;
    /**
     * byte array.
     */
    byte[] data;

    /**
     * Desfire card object.
     */
    private IDESFireEV1 desFireEV1;

    private IDESFireEV2 desFireEV2;
    /**
     * Checkbox for write select
     */
    CheckBox mCheckToWrite;
    /**
     * Constant for permission
     */
    private static final int STORAGE_PERMISSION_WRITE = 113;

    /**
     * Android Handler for handling messages from the threads.
     */
    private static Handler mHandler;

    private boolean bWriteAllowed = true;
    private KeyInfoProvider infoProvider;

    private boolean mIsPerformingCardOperations = false;
    private CardType mCardType = CardType.UnknownCard;
    private String process = Constants.SCAN;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reader);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        publicKey = getIntent().getStringExtra(Constants.PUB_KEY);
        signature = getIntent().getStringExtra(Constants.SIGNATURE);
        challenge = getIntent().getStringExtra(Constants.CHALLENGE);

        boolean readPermission = (ContextCompat.checkSelfPermission(ReaderActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!readPermission) {
            ActivityCompat.requestPermissions(ReaderActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_WRITE
            );
        }

        process = getIntent().getStringExtra(Constants.INTENT_PROCESS_TYPE);
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            if(process != null){
                if(process.equals(Constants.SCAN)){
                    mFragment = ScanFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                          .add(R.id.fragment_container, mFragment,SCAN_FRAGMENT_TAG).commit();
                }else{
                    mFragment = ProvisioningFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                          .add(R.id.fragment_container, mFragment,CREATE_FRAGMENT_TAG).commit();
                }
            }
        }

        /* Initialize the library and register to this activity */
        initializeLibrary();

        initializeKeys();

		/* Initialize the Cipher and init vector of 16 bytes with 0xCD */
        initializeCipherinitVector();

		/* Get text view handle to be used further */
        initializeView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(int title){
        getSupportActionBar().setTitle(title);
    }

    public void finishProcess(){
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initializeKeys() {
        infoProvider = KeyInfoProvider.getInstance(getApplicationContext());

        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        boolean keysStoredFlag = sharedPrefs.getBoolean(EXTRA_KEYS_STORED_FLAG, false);
        if (!keysStoredFlag) {
            //Set Key stores the key in persistent storage, this method can be called only once if key for a given alias does not change.
            byte[] ulc24Keys = new byte[24];
            System.arraycopy(SampleAppKeys.KEY_2KTDES_ULC, 0, ulc24Keys, 0, SampleAppKeys.KEY_2KTDES_ULC.length);
            System.arraycopy(SampleAppKeys.KEY_2KTDES_ULC, 0, ulc24Keys, SampleAppKeys.KEY_2KTDES_ULC.length, 8);
            infoProvider.setKey(ALIAS_KEY_2KTDES_ULC, SampleAppKeys.EnumKeyType.EnumDESKey, ulc24Keys);

            infoProvider.setKey(ALIAS_KEY_2KTDES, SampleAppKeys.EnumKeyType.EnumDESKey, SampleAppKeys.KEY_2KTDES);
            infoProvider.setKey(ALIAS_KEY_AES128, SampleAppKeys.EnumKeyType.EnumAESKey, SampleAppKeys.KEY_AES128);
            infoProvider.setKey(ALIAS_KEY_AES128_ZEROES, SampleAppKeys.EnumKeyType.EnumAESKey, SampleAppKeys.KEY_AES128_ZEROS);
            infoProvider.setKey(ALIAS_DEFAULT_FF, SampleAppKeys.EnumKeyType.EnumMifareKey, SampleAppKeys.KEY_DEFAULT_FF);

            sharedPrefs.edit().putBoolean(EXTRA_KEYS_STORED_FLAG, true).commit();
            //If you want to store a new key after key initialization above, kindly reset the flag EXTRA_KEYS_STORED_FLAG to false in shared preferences.
        }


        objKEY_2KTDES_ULC = infoProvider.getKey(ALIAS_KEY_2KTDES_ULC, SampleAppKeys.EnumKeyType.EnumDESKey);
        objKEY_2KTDES = infoProvider.getKey(ALIAS_KEY_2KTDES, SampleAppKeys.EnumKeyType.EnumDESKey);
        objKEY_AES128 = infoProvider.getKey(ALIAS_KEY_AES128, SampleAppKeys.EnumKeyType.EnumAESKey);
        default_zeroes_key = infoProvider.getKey(ALIAS_KEY_AES128_ZEROES, SampleAppKeys.EnumKeyType.EnumAESKey);
        default_ff_key = infoProvider.getMifareKey(ALIAS_DEFAULT_FF);
    }

    /**
     * Initializing the widget, and Get text view handle to be used further.
     */
    private void initializeView() {

    }

    /**
     * Initialize the library and register to this activity.
     */
    @TargetApi(19)
    private void initializeLibrary() {
        libInstance = NxpNfcLib.getInstance();
        try {
            libInstance.registerActivity(this, packageKey);
        } catch (NxpNfcLibException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize the Cipher and init vector of 16 bytes with 0xCD.
     */

    private void initializeCipherinitVector() {

		/* Initialize the Cipher */
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

		/* set Application Master Key */
        bytesKey = KEY_APP_MASTER.getBytes();

		/* Initialize init vector of 16 bytes with 0xCD. It could be anything */
        byte[] ivSpec = new byte[16];
        Arrays.fill(ivSpec, (byte) 0xCD);
        iv = new IvParameterSpec(ivSpec);

    }

    /**
     * (non-Javadoc).
     *
     * @param intent NFC intent from the android framework.
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    public void onNewIntent(final Intent intent) {
        cardLogic(intent);
        super.onNewIntent(intent);
    }


    private void cardLogic(final Intent intent) {
        CardType type = CardType.UnknownCard;
        try {
            type = libInstance.getCardType(intent);
        } catch (NxpNfcLibException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        switch (type) {
            case DESFireEV1:
                mCardType = CardType.DESFireEV1;
                desFireEV1 = DESFireFactory.getInstance().getDESFire(libInstance.getCustomModules());
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                if(tag != null){
                    tagId = Util.bytesToHex(tag.getId());
                }
                try {

                    desFireEV1.getReader().connect();
                    desFireEV1.getReader().setTimeout(2000);
                    desfireEV1CardLogic();

                } catch (Throwable t) {
                    t.printStackTrace();
                    showMessage("Unknown Error Tap Again!", 't');
                }
                break;
            case DESFireEV2:
                mCardType = CardType.DESFireEV2;
                showMessage("DESFireEV2 Card detected.", 't');
                tv.setText(" ");
                showMessage("Card Detected : DESFireEV2", 'n');
                desFireEV2 = DESFireFactory.getInstance().getDESFireEV2(libInstance.getCustomModules());
                try {
                    desFireEV2.getReader().connect();
                    desfireEV2CardLogic();

                } catch (Throwable t) {
                    t.printStackTrace();
                    showMessage("Unknown Error Tap Again!", 't');
                }
                break;
        }
    }

    /**
     * DESFire Pre Conditions.
     * <p/>
     * PICC Master key should be factory default settings, (ie 16 byte All zero
     * Key ).
     * <p/>
     */
    private void desfireEV1CardLogic() {

        byte[] myKey = new byte[] {
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        Key key = new SecretKeySpec(myKey, "DESede");
        KeyData keyData = new KeyData();
        keyData.setKey( key);

        byte[] appId = new byte[]{0x12, 0x00, 0x00};
        byte[] appId_2 = new byte[]{0x03, 0x02, 0x01};

        int fileSize_pubKey  = 64;
        int fileSize_hashMsg = 32;
        int fileSize_privKey = 64;

        int timeOut = 1000;
        int fileNo = 0;
        int fileNo_2 = 1;

        showMessage("Card Detected : " + desFireEV1.getType().getTagName(), 'n');

        try {


            if (process.equals(Constants.CREATE_TWIN)) {

                Log.d(TAG,"Proccess is create twin");
                desFireEV1.getReader().setTimeout(timeOut);
                desFireEV1.selectApplication(0);

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);
                desFireEV1.format();
                EV1ApplicationKeySettings.Builder appsetbuilder = new EV1ApplicationKeySettings.Builder();

                EV1ApplicationKeySettings appsettings = appsetbuilder.setAppKeySettingsChangeable(true)
                      .setAppMasterKeyChangeable(true)
                      .setAuthenticationRequiredForApplicationManagement(false)
                      .setAuthenticationRequiredForDirectoryConfigurationData(false)
                      .setKeyTypeOfApplicationKeys(KeyType.TWO_KEY_THREEDES).build();

                desFireEV1.createApplication(appId, appsettings);
                desFireEV1.selectApplication(appId);

                desFireEV1.createFile(fileNo, new DESFireFile.StdDataFileSettings(
                      IDESFireEV1.CommunicationType.Plain, (byte)0x00, (byte)0, (byte)0, (byte)0, fileSize_pubKey));

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);

                // writeData command:
                // 1 byte : file number
                // 3 bytes : offset
                // 3 bytes : length
                // 0 to 52 bytes : datas to write


                desFireEV1.writeData(0, 0, Util.hexStringToByteArray(publicKey));
                publicKey = Util.bytesToHex(desFireEV1.readData(0,0,64));
                showMessage(
                      "Pub Key read from the card : " + Util.bytesToHex(desFireEV1.readData(0, 0,
                            64)), 'd');

                //desFireEV1.selectApplication(0);

            /*readAccess - Take values from 0x00 to 0xF.
            0xE : free access.
            0xF : read access denied.
            0x00 to 0x0d -- authentication required with the key number for read access.

            writeAccess - Take values from 0x00 to 0xF.
            0xE : free access.
            0xF : read access denied.
            0x00 to 0x0d -- authentication required with the key number for write access.

            readWriteAccess - Take values from 0x00 to 0xF.
            0xE : free access.
            0xF : read access denied.
            0x00 to 0x0d -- authentication required with the key number for read and write access.

            changeAccess - Take values from 0x00 to 0xF.
            0xE : free access.
            0xF : read access denied.
            0x00 to 0x0d -- authentication required with the key number for changing the access rights of the file.*/

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);

                desFireEV1.createFile(fileNo_2, new DESFireFile.StdDataFileSettings(
                      IDESFireEV1.CommunicationType.Plain, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, fileSize_hashMsg));

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);

                desFireEV1.writeData(1, 0, Util.hexStringToByteArray(challenge));
                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);
                challenge = Util.bytesToHex(desFireEV1.readData(1, 0,32));
                showMessage(
                      "HashMsg Read from the card : "
                            + Util.bytesToHex(desFireEV1.readData(1, 0,
                            32)), 'd');
                showMessage(
                      "Free Memory of the Card : " + desFireEV1.getFreeMemory(),
                      'd');

            /* To write to fiules in encrypted mode via AES128 first authenticate with 3DES.
               Then select again the application the file belongs to.
             */

                desFireEV1.selectApplication(0);

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);

            /* Then change the setting params for the to be created application .
               Especially change the type of the application's key to AES128.
             */

                EV1ApplicationKeySettings.Builder appsetbuilder_2 = new EV1ApplicationKeySettings.Builder();

                EV1ApplicationKeySettings appsettings_2 = appsetbuilder_2.setAppKeySettingsChangeable(true)
                      .setAppMasterKeyChangeable(true)
                      .setAuthenticationRequiredForApplicationManagement(false)
                      .setAuthenticationRequiredForDirectoryConfigurationData(false)
                      .setKeyTypeOfApplicationKeys(KeyType.AES128).build();

            /* Create the new application with the recently defined application settings .
               Then select the new application with its app ID.
             */

                desFireEV1.createApplication(appId_2, appsettings_2);
                desFireEV1.selectApplication(appId_2);

            /* Authenticate again.But this time with the  pre-defined AES128 key.
               Then create the new encrypted file and define the permissions for the file.
             */

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.AES, KeyType.AES128, default_zeroes_key);
                desFireEV1.createFile(fileNo, new DESFireFile.StdDataFileSettings(
                      IDESFireEV1.CommunicationType.Plain, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, fileSize_privKey));

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.AES, KeyType.AES128, default_zeroes_key);

                desFireEV1.writeData(0, 0, Util.hexStringToByteArray(signature));
                signature = Util.bytesToHex(desFireEV1.readData(0, 0,64));
                showMessage(
                      "Signature Key read from the card : "
                            + Util.bytesToHex(desFireEV1.readData(0, 0,
                            64)), 'd');

                startProcess();

            }else{

                Log.d(TAG,"Proccess is scan");

                desFireEV1.getReader().setTimeout(timeOut);
                desFireEV1.selectApplication(0);

                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);

                if(desFireEV1.getApplicationIDs().length > 1){
                    Log.d(TAG,"Application ids is more than 0");

                    desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);
                    desFireEV1.selectApplication(appId);


                    desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);
                    publicKey = Util.bytesToHex(desFireEV1.readData(0,0,64));
                    Log.d(TAG,"Reader public key "+publicKey);


                    desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);
                    challenge = Util.bytesToHex(desFireEV1.readData(1, 0,32));
                    Log.d(TAG,"Reader challenge "+challenge);

                    desFireEV1.selectApplication(0);

                    desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);
                    desFireEV1.selectApplication(appId_2);

                    desFireEV1.authenticate(0, IDESFireEV1.AuthType.AES, KeyType.AES128, default_zeroes_key);
                    signature = Util.bytesToHex(desFireEV1.readData(0, 0,64));
                    Log.d(TAG,"Reader signature "+signature);

                    startProcess();
                }else{

                    Log.d(TAG,"No application ids found");

                    showNoDataFound();
                }

                //Format tag
//                desFireEV1.getReader().setTimeout(timeOut);
//                desFireEV1.selectApplication(0);
//
//                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);
//
//                desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, keyData);
//                desFireEV1.format();

            }

            desFireEV1.getReader().close();

            // Set the custom path where logs will get stored, here we are setting the log folder DESFireLogs under
            // external storage.
            String spath = Environment.getExternalStorageDirectory().getPath() + File.separator + "DESFireLogs";
            NxpLogUtils.setLogFilePath(spath);
            // if you don't call save as below , logs will not be saved.
            NxpLogUtils.save();

        } catch (Exception e) {
//            showMessage("IOException occurred... check LogCat", 't');
            e.printStackTrace();
        }

    }

    private void desfireEV2CardLogic() {
        byte[] appId = new byte[]{0x12, 0x00, 0x00};
        int fileSize = 100;
        byte[] data = new byte[]{0x11, 0x11, 0x11, 0x11,
                0x11};
        int timeOut = 2000;
        int fileNo = 0;

        tv.setText(" ");
        showMessage("Card Detected : " + desFireEV2.getType().getTagName(), 'n');

        try {
            desFireEV2.getReader().setTimeout(timeOut);
            showMessage(
                    "Version of the Card : "
                            + Utilities.dumpBytes(desFireEV2.getVersion()),
                    'd');
            showMessage(
                    "Existing Applications Ids : " + Arrays.toString(desFireEV2.getApplicationIDs()),
                    'd');


            desFireEV2.selectApplication(0);

            desFireEV2.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.THREEDES, objKEY_AES128);

            /* Do the following only if write checkbox is selected */
            if (bWriteAllowed) {
                desFireEV2.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.THREEDES, objKEY_2KTDES);

                desFireEV2.getReader().setTimeout(timeOut);
                desFireEV2.format();
                EV1ApplicationKeySettings.Builder appsetbuilder = new EV1ApplicationKeySettings.Builder();

                EV1ApplicationKeySettings appsettings = appsetbuilder.setAppKeySettingsChangeable(true)
                        .setAppMasterKeyChangeable(true)
                        .setAuthenticationRequiredForApplicationManagement(false)
                        .setAuthenticationRequiredForDirectoryConfigurationData(false)
                        .setKeyTypeOfApplicationKeys(KeyType.TWO_KEY_THREEDES).build();

                desFireEV2.createApplication(appId, appsettings);
                desFireEV2.selectApplication(appId);

                desFireEV2.createFile(fileNo, new DESFireFile.StdDataFileSettings(
                        IDESFireEV1.CommunicationType.Plain, (byte) 0,(byte) 0, (byte)0, (byte)0, fileSize));

                desFireEV2.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.TWO_KEY_THREEDES, objKEY_2KTDES);
                desFireEV2.writeData(0, 0, data);
                showMessage(
                        "Data Read from the card : "
                                + Utilities.dumpBytes(desFireEV2.readData(0, 0,
                                5)), 'd');
                showMessage(
                        "Free Memory of the Card : " + desFireEV2.getFreeMemory(),
                        'd');
                desFireEV2.getReader().close();
            }

            // Set the custom path where logs will get stored, here we are setting the log folder DESFireLogs under
            // external storage.
            String spath = Environment.getExternalStorageDirectory().getPath() + File.separator + "DESFireLogs";
            NxpLogUtils.setLogFilePath(spath);
            // if you don't call save as below , logs will not be saved.
            NxpLogUtils.save();

        } catch (Exception e) {
            showMessage("IOException occurred... check LogCat", 't');
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(libInstance != null){
            libInstance.stopForeGroundDispatch();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(libInstance != null){
            libInstance.startForeGroundDispatch();
        }
    }

    /**
     * This will display message in toast or logcat or on screen or all three.
     *
     * @param str   String to be logged or displayed
     * @param where 't' for Toast; 'l' for Logcat; 'd' for Display in UI; 'n' for
     *              logcat and textview 'a' for All
     */
    protected void showMessage(final String str, final char where) {

        switch (where) {

            case 't':
                Toast.makeText(ReaderActivity.this, "\n" + str, Toast.LENGTH_SHORT)
                        .show();
                break;
            case 'l':
                NxpLogUtils.i(TAG, "\n" + str);
                break;
            case 'd':
                NxpLogUtils.i(TAG, "Data: " + str);
                break;
            case 'a':
                Toast.makeText(ReaderActivity.this, "\n" + str, Toast.LENGTH_SHORT)
                        .show();
                NxpLogUtils.i(TAG, "\n" + str);
                break;
            case 'n':
                NxpLogUtils.i(TAG, "Dump Data: " + str);
                break;
            default:
                break;
        }
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_WRITE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ReaderActivity.this, "Requested permission granted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ReaderActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }

    //Start validation o creation depends of the proccess selected by the user
    private void startProcess(){
        if(process.equals(Constants.SCAN)){
            ((ScanFragment)mFragment).startScan();
            validateTransaction();
        }else{
            if(mFragment instanceof ScanFragment){
                goToCreateDigitalTwin();
            }else{
                ((ProvisioningFragment)mFragment).adaptUItoResult(tagId);
            }
        }
    }

    private void validateTransaction(){
        RCApiManager.validate(publicKey,signature,challenge, new Callback<RCApiResponse>() {
            @Override
            public void onResponse(Call<RCApiResponse> call, Response<RCApiResponse> response) {
                RCApiResponse apiResponse = response.body();
                if(apiResponse.isSuccessful()){

                    Gson gson = new Gson();
                    Intent intent = new Intent(ReaderActivity.this,ScannedTwinActivity.class);
                    String transaction = gson.toJson(apiResponse.getData());
                    intent.putExtra(Constants.INTENT_TRANSACTION,transaction);
                    startActivity(intent);
                    finish();
                }else{
                    if(apiResponse.hasErrors()){
                        showCreateDialog();
                    }else{
                        ((ScanFragment)mFragment).stopScan();
                    }
                }
            }

            @Override
            public void onFailure(Call<RCApiResponse> call, Throwable t) {

            }
        });
    }

    public void goToCreateDigitalTwin(){
        Intent intent = new Intent(ReaderActivity.this,CreateDigitalTwinActivity.class);

        intent.putExtra(Constants.PUB_KEY,publicKey);
        intent.putExtra(Constants.SIGNATURE,signature);
        intent.putExtra(Constants.CHALLENGE,challenge);
        startActivity(intent);
        finish();
    }

    public void getCredentials(){
        Log.d(TAG,"Getting credentials");

        RCApiManager.getCredentials(new Callback<RCApiResponse>() {
            @Override
            public void onResponse(Call<RCApiResponse> call, Response<RCApiResponse> response) {
                RCApiResponse apiResponse = response.body();

                if(apiResponse.isSuccessful()){
                    Gson gson = new Gson();
                    Credentials credentials = gson.fromJson(apiResponse.getStringData(), Credentials.class);
                    publicKey = credentials.getPublicKey();
                    challenge = credentials.getChallenge();
                    signature = credentials.getSignature();

                    process = Constants.CREATE_TWIN;
                }
            }

            @Override
            public void onFailure(Call<RCApiResponse> call, Throwable t) {

            }
        });
    }

    private void showCreateDialog(){
        DialogFragment newFragment = AlertDialogFragment.newInstance(
              R.string.dialog_tx_not_found_title,R.string.dialog_data_not_found_text,true);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void showNoDataFound(){
        DialogFragment newFragment = AlertDialogFragment.newInstance(
              R.string.dialog_data_not_found_title,R.string.dialog_data_not_found_text,false);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}
