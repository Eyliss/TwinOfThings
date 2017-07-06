package com.twinofthings.activities;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twinofthings.R;
import com.twinofthings.helpers.CircleImageView;
import com.twinofthings.models.Transaction;
import com.twinofthings.utils.Constants;
import com.twinofthings.utils.Util;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TwinCreatedActivity extends AppCompatActivity {

    private Button mBackButton;
    private Button mScanTag;
    private Toolbar mToolbar;
    private CircleImageView mThumbnail;

//    private Transaction mTransaction;
    private String mThumbnailEncoded;

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
        mThumbnailEncoded = getIntent().getStringExtra(Constants.INTENT_IMAGE);

        Gson gson = new Gson();
        String post = getIntent().getExtras().getString(Constants.INTENT_TRANSACTION);
//        mTransaction = gson.fromJson(post, Transaction.class);

        bindViews();

        setProductInfo();
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

        mThumbnail = (CircleImageView) findViewById(R.id.thumbnail);
    }

    private void setProductInfo(){

        //Set the scanned product thumbnail converting the received base64 string into a bitmap
        if(mThumbnailEncoded != null){
            mThumbnail.setImageBitmap(Util.decodeBase64toBitmap(mThumbnailEncoded));
        }
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
