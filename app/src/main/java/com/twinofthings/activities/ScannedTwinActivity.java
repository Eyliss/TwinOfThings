package com.twinofthings.activities;

import com.google.gson.Gson;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.twinofthings.R;
import com.twinofthings.models.Transaction;
import com.twinofthings.utils.Constants;

public class ScannedTwinActivity extends Activity {

    private Button mBackButton;
    private TextView mRegisteredDate;
    private TextView mId;
    private TextView mName;
    private TextView mDescription;
    private TextView mOwner;
    private TextView mObjectId;
    private TextView mMaterial;
    private TextView mColor;
    private TextView mComments;

    private Transaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_scanned_twin);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        getActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));

        Gson gson = new Gson();
        String post = getIntent().getExtras().getString(Constants.INTENT_TRANSACTION);
        mTransaction = gson.fromJson(post, Transaction.class);

        bindViews();

//        setProductInfo();

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
//        mDescription = (TextView)findViewById(R.id.tv_product_description);
        mOwner = (TextView)findViewById(R.id.tv_owner);
        mObjectId = (TextView)findViewById(R.id.tv_object_id);
//        mMaterial = (TextView)findViewById(R.id.tv_material);
//        mColor = (TextView)findViewById(R.id.tv_color);
        mComments = (TextView)findViewById(R.id.tv_comments);
    }

    private void setProductInfo(){

        mRegisteredDate.setText(mTransaction.getMetadata().getTimestamp());
        mId.setText(mTransaction.getId());
        mOwner.setText(mTransaction.getMetadata().getUserId());
        mObjectId.setText(mTransaction.getId());
        mComments.setText(mTransaction.getMetadata().getDescription());
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
