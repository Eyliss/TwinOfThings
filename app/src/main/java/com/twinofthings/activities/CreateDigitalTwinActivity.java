package com.twinofthings.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.twinofthings.R;
import com.twinofthings.api.RCApiManager;
import com.twinofthings.api.RCApiResponse;
import com.twinofthings.utils.Constants;

import java.sql.Time;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDigitalTwinActivity extends Activity {

    private String publicKey;
    private String challenge;
    private String signature;

    private EditText mName;
    private DatePicker mDatePicker;
    private EditText mOwner;
    private TextView mLocation;
    private EditText mComments;
    private Button mCreateTwin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_digital_twin);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
              .getColor(android.R.color.transparent)));

        publicKey = getIntent().getStringExtra(Constants.INTENT_PUB_KEY);
        challenge = getIntent().getStringExtra(Constants.INTENT_SIGNATURE);
        signature = getIntent().getStringExtra(Constants.INTENT_CHALLENGE);

        mName = (EditText) findViewById(R.id.product_name);
        mDatePicker = (DatePicker) findViewById(R.id.dp_timestamp);
        mOwner = (EditText) findViewById(R.id.owner);
        mLocation = (TextView) findViewById(R.id.twin_location);
        mComments = (EditText) findViewById(R.id.comments);
        mCreateTwin = (Button) findViewById(R.id.create_digital_twin_button);

        mCreateTwin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTwinDataToServer();
            }
        });
    }

    private void sendTwinDataToServer(){
        String name = mName.getText().toString();
        String timestamp = getDateFromDatePicker();
        String owner = mOwner.getText().toString();

        RCApiManager.provision(publicKey,signature,challenge, , new Callback<RCApiResponse>() {
            @Override
            public void onResponse(Call<RCApiResponse> call, Response<RCApiResponse> response) {
                showScannedTwinInformation();
            }

            @Override
            public void onFailure(Call<RCApiResponse> call, Throwable t) {

            }
        });
    }

    public String getDateFromDatePicker(){
        int day = mDatePicker.getDayOfMonth();
        int month = mDatePicker.getMonth();
        int year =  mDatePicker.getYear();

        return day+"/"+month+"/"+year;
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
