package com.twinofthings.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twinofthings.R;
import com.twinofthings.sezamecore.SezameRegistrationHelper;
import com.twinofthings.sezamecore.utils.P;
import com.twinofthings.utils.Util;

public class BioAuthenticationActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mQuestion1;
    private EditText mQuestion2;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_authentication);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        P.init(this);

        mEmail = (EditText) findViewById(R.id.user_email);
        mQuestion1 = (EditText) findViewById(R.id.register_question1);
        mQuestion2 = (EditText) findViewById(R.id.register_question2);
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
        mQuestion1.setError(null);
        mQuestion2.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail.getText().toString().trim();
        String question1 = mQuestion1.getText().toString();
        String question2 = mQuestion2.getText().toString();

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

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(question1)) {
            mQuestion1.setError(getString(R.string.error_empty_field));
            focusView = mQuestion1;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(question2)) {
            mQuestion2.setError(getString(R.string.error_empty_field));
            focusView = mQuestion2;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            registerUser(email,question1,question2);
        }
    }

    private void registerUser(String email, String q1, String q2){
        SezameRegistrationHelper.instance(getResources()).register(email, pushToken)
              .subscribe(success -> {
                        if (success) {
                            //registration successful
                        } else {
                            //Device recovery, Email already registered
                        }
                        SezameRegistrationHelper.instance().cleanUp();
                    }, throwable -> {
                        //failed
                        SezameRegistrationHelper.instance().cleanUp();
                    }
              );
    }
}
