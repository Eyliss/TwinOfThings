package com.twinofthings.activities;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
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
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static android.icu.lang.UProperty.INT_START;

public class ScannedTwinActivity extends AppCompatActivity {

    private Button mBackButton;
    private TextView mRegisteredDate;
    private TextView mTransactionId;
    private CircleImageView mThumbnail;

    private TextView mProductName;
//    private TextView mProductSubline;
    private TextView mOwner;
    private TextView mObjectId;
    private TextView mMaterial;
    private TextView mBrand;
    private TextView mComments;


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
        mTransactionId = (TextView)findViewById(R.id.tv_pup_id);
        mTransactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransactionLink();
            }
        });
        mProductName = (TextView)findViewById(R.id.tv_product_name);
//        mProductSubline = (TextView)findViewById(R.id.tv_product_subline);
        mOwner = (TextView)findViewById(R.id.tv_owner);
        mObjectId = (TextView)findViewById(R.id.tv_object_id);
        mMaterial = (TextView)findViewById(R.id.tv_material);
        mBrand = (TextView)findViewById(R.id.tv_brand);
        mComments = (TextView)findViewById(R.id.tv_comments);
        mThumbnail = (CircleImageView) findViewById(R.id.thumbnail);
        mThumbnail.setBorderColor(getResources().getColor(R.color.colorPrimaryDark));
        mThumbnail.setBorderWidth(5);
    }

    private void setProductInfo(){

        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getAssets(), "fonts/CooperHewitt-Bold.otf"));

        String timestamp = mTransaction.getMetadata().getTimestamp();
        SpannableStringBuilder str = new SpannableStringBuilder(timestamp);
        str.setSpan(new RelativeSizeSpan(1.1f), 0, timestamp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(typefaceSpan, 0, timestamp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mRegisteredDate.setText(str);

        String id = mTransaction.getId();
        str = new SpannableStringBuilder(id);
        str.setSpan(new RelativeSizeSpan(1.1f), 0, id.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(typefaceSpan, 0, id.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTransactionId.setText(str);

        mProductName.setText(mTransaction.getMetadata().getProductName());
//        mProductSubline.setText(mTransaction.getMetadata().getProductSubline());
        mOwner.setText(mTransaction.getMetadata().getOwnerName());
        mObjectId.setText(mTransaction.getMetadata().getSerialId());
        mMaterial.setText(mTransaction.getMetadata().getMaterial());
        mBrand.setText(mTransaction.getMetadata().getBrandName());
        mComments.setText(mTransaction.getMetadata().getCommentsDetail());

        //Set the scanned product thumbnail converting the received base64 string into a bitmap
        String thumbnail = mTransaction.getMetadata().getThumbnail().getContent();
        if(thumbnail != null){
            mThumbnail.setImageBitmap(Util.decodeBase64toBitmap(thumbnail));
        }
    }

    private void openTransactionLink(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://34.251.133.19:8000/webinterface/tx-info/"+mTransaction.getId()));
        startActivity(browserIntent);
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
