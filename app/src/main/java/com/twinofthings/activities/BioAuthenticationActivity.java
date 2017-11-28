package com.twinofthings.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twinofthings.R;
import com.twinofthings.sezamecore.SezameRegistrationHelper;
import com.twinofthings.sezamecore.utils.P;
import com.twinofthings.utils.Constants;
import com.twinofthings.utils.Util;

public class BioAuthenticationActivity extends AppCompatActivity {

    private EditText mEmail;
    private Button mRegister;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_authentication);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        P.init(this);
        mEmail = (EditText) findViewById(R.id.user_email);
        mRegister = (Button) findViewById(R.id.register_user);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempRegistration();
            }
        });
    }

    private void attempRegistration(){
        // Reset errors.
        mEmail.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!Util.isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            registerUser(email);
        }
    }

    private void registerUser(String email){
        progress = ProgressDialog.show(this, "Loading", "Wait while loading...");
        String token = P.instance().getString(Constants.FCM_TOKEN);
        SezameRegistrationHelper.instance(getResources()).register(email, token)
              .subscribe(success -> {
                        if (success) {
                            Log.d("Authentication","User authenticated");
                            goToMainScreen();
                        } else {
                            goToMainScreen();
                            Log.d("Authentication","Device recovery");
                        }
                        SezameRegistrationHelper.instance().cleanUp();
                    }, throwable -> {
                        //failed
                        SezameRegistrationHelper.instance().cleanUp();
                    }
              );
    }

    private void goToMainScreen(){
        progress.dismiss();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
