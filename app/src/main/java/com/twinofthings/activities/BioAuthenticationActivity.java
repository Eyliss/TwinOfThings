package com.twinofthings.activities;

import com.google.gson.Gson;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twinofthings.R;
import com.twinofthings.helpers.RCFingerprintHandler;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BioAuthenticationActivity extends AppCompatActivity {


    private static final String KEY_NAME = "rc_key";

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    private TextView mAuthResult;
    private ImageView mAuthIcon;

    public interface AuthenticationListener {
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result);
        public void onAuthenticationFailed();
    }

    private AuthenticationListener mAuthenticationListener = new AuthenticationListener() {
        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            mAuthResult.setText(R.string.auth_succeed);
            mAuthResult.setTextColor(getResources().getColor(R.color.teal));
            mAuthIcon.setImageResource(R.drawable.ic_check_circle_black_24dp);
            Intent intent = new Intent(BioAuthenticationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onAuthenticationFailed() {
            mAuthResult.setText(R.string.auth_failed);
            mAuthResult.setTextColor(getResources().getColor(R.color.deep_orange));
            mAuthIcon.setImageResource(R.drawable.ic_auth_error);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_authentication);

        mAuthResult = (TextView) findViewById(R.id.tv_auth_result);
        mAuthIcon = (ImageView) findViewById(R.id.iv_fingerprint);

        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if (!keyguardManager.isKeyguardSecure()) {

            Toast.makeText(this,"Lock screen security not enabled in Settings",Toast.LENGTH_LONG).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);;
            startActivity(intent);
            finish();
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);;
            startActivity(intent);
            finish();
        }

        generateKey();

        if (cipherInit()) {
            cryptoObject = new FingerprintManager.CryptoObject(cipher);
            RCFingerprintHandler helper = new RCFingerprintHandler(this,mAuthenticationListener);
            helper.startAuth(fingerprintManager, cryptoObject);
        }
    }

    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException |
              NoSuchProviderException e) {
            throw new RuntimeException(
                  "Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                  KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                  .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                  .setUserAuthenticationRequired(true)
                  .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                  .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
              InvalidAlgorithmParameterException
              | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"+ KeyProperties.BLOCK_MODE_CBC + "/"+ KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
              NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                  null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
              | UnrecoverableKeyException | IOException
              | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
