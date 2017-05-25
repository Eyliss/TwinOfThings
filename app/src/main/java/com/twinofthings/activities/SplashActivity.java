package com.twinofthings.activities;

import com.crashlytics.android.Crashlytics;
import com.twinofthings.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends Activity {
	
	/** Splash screen timer.*/
    public static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Fabric.with(this, new Crashlytics());

        new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
