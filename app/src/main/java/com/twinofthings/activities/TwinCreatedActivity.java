package com.twinofthings.activities;

import com.google.gson.Gson;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.twinofthings.R;
import com.twinofthings.api.RCApiManager;
import com.twinofthings.api.RCApiResponse;
import com.twinofthings.models.Credentials;
import com.twinofthings.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwinCreatedActivity extends AppCompatActivity {

    private Button mBackButton;
    private Button mScanTag;
    private Toolbar mToolbar;

    private String publicKey = "";
    private String signature = "";
    private String challenge = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twin_created);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        publicKey = getIntent().getStringExtra(Constants.PUB_KEY);
        signature = getIntent().getStringExtra(Constants.SIGNATURE);
        challenge = getIntent().getStringExtra(Constants.CHALLENGE);

        bindViews();
    }

    private void bindViews(){
        mBackButton = (Button)findViewById(R.id.back_to_start);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close the screen and return to main screen
                TwinCreatedActivity.this.finish();
            }
        });

        mScanTag = (Button)findViewById(R.id.scan_tag_button);
        mScanTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReaderScreen();
            }
        });
    }

    private void goToReaderScreen(){
        Intent intent = new Intent(TwinCreatedActivity.this,ReaderActivity.class);
        intent.putExtra(Constants.PUB_KEY,publicKey);
        intent.putExtra(Constants.SIGNATURE,signature);
        intent.putExtra(Constants.CHALLENGE,challenge);
        intent.putExtra(Constants.INTENT_PROCESS_TYPE,Constants.SCAN);
        startActivity(intent);
        finish();
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

}
