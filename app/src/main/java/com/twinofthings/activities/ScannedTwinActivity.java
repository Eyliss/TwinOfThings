package com.twinofthings.activities;

import com.google.gson.Gson;

import android.os.Bundle;
import android.app.Activity;
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

public class ScannedTwinActivity extends AppCompatActivity {

    private Button mBackButton;
    private TextView mRegisteredDate;
    private TextView mId;
    private TextView mName;
    private TextView mOwner;
    private TextView mLocation;
    private TextView mComments;
    private CircleImageView mThumbnail;


    private Transaction mTransaction;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanned_twin);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Gson gson = new Gson();
        String post = getIntent().getExtras().getString(Constants.INTENT_TRANSACTION);
        mTransaction = gson.fromJson(post, Transaction.class);

        bindViews();

        setProductInfo();

    }

    private void bindViews(){
        mBackButton = (Button)findViewById(R.id.back_to_start);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannedTwinActivity.this.finish();
            }
        });
        mRegisteredDate = (TextView)findViewById(R.id.tv_registered_date);
        mId = (TextView)findViewById(R.id.tv_pup_id);
        mName = (TextView)findViewById(R.id.tv_product_name);
        mLocation = (TextView)findViewById(R.id.tv_location);
        mOwner = (TextView)findViewById(R.id.tv_owner);
        mComments = (TextView)findViewById(R.id.tv_comments);
        mThumbnail = (CircleImageView) findViewById(R.id.thumbnail);
    }

    private void setProductInfo(){

        mId.setText(getString(R.string.pup_id,mTransaction.getId()));
        mRegisteredDate.setText(getString(R.string.registered,mTransaction.getMetadata().getTimestamp()));
        mName.setText(mTransaction.getMetadata().getName());
        mOwner.setText(mTransaction.getMetadata().getUserId());
        mComments.setText(mTransaction.getMetadata().getDescription());
        mLocation.setText(mTransaction.getMetadata().getLocation());

        //Set the scanned product thumbnail converting the received base64 string into a bitmap
        String thumbnail = mTransaction.getMetadata().getThumbnail().getContent();
        if(thumbnail != null){
            mThumbnail.setImageBitmap(Util.decodeBase64toBitmap(thumbnail));
        }
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
