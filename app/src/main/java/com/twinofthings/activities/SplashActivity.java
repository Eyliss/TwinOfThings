package com.twinofthings.activities;

import com.crashlytics.android.Crashlytics;
import com.twinofthings.R;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.security.AccessController.getContext;

public class SplashActivity extends Activity {
	
	/** Splash screen timer.*/
    public static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Fabric.with(this, new Crashlytics());

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), BioAuthenticationActivity.class);;
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private boolean checkFingerprintPermission() {
        return  (ContextCompat.checkSelfPermission(SplashActivity.this,
              Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED);
    }
}
