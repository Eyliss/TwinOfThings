package com.twinofthings.helpers;

import com.twinofthings.activities.BioAuthenticationActivity;
import com.twinofthings.activities.MainActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Eyliss on 11/28/17.
 */

public class RCFingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context appContext;
    private BioAuthenticationActivity.AuthenticationListener mAuthenticationListener;

    public RCFingerprintHandler(Context context, BioAuthenticationActivity.AuthenticationListener authenticationListener) {
        appContext = context;
        mAuthenticationListener = authenticationListener;
    }

    public void startAuth(FingerprintManager manager,
                          FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();

        if (ActivityCompat.checkSelfPermission(appContext,Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {
        mAuthenticationListener.onAuthenticationFailed();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        mAuthenticationListener.onAuthenticationFailed();
    }

    @Override
    public void onAuthenticationFailed() {
        mAuthenticationListener.onAuthenticationFailed();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        mAuthenticationListener.onAuthenticationSucceeded(result);

    }
}
