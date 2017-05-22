package com.twinofthings.activities;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDigitalTwinActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final static int REQUEST_LOCATION_PERMISSION = 0x17;

    private String publicKey;
    private String challenge;
    private String signature;

    private EditText mName;
    private DatePicker mDatePicker;
    private EditText mOwner;
    private TextView mLocation;
    private EditText mComments;
    private Button mCreateTwin;
    private Location mLastLocation;
    private String locationName;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_create_digital_twin);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        getActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));

        publicKey = getIntent().getStringExtra(Constants.INTENT_PUB_KEY);
        signature = getIntent().getStringExtra(Constants.INTENT_SIGNATURE);
        challenge = getIntent().getStringExtra(Constants.INTENT_CHALLENGE);

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

        locationName = getLocationName();
        mLocation.setText(locationName);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                  .addConnectionCallbacks(this)
                  .addOnConnectionFailedListener(this)
                  .addApi(LocationServices.API)
                  .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private void sendTwinDataToServer() {
        String name = mName.getText().toString();
        String timestamp = getDateFromDatePicker();
        String owner = mOwner.getText().toString();
        String comments = mComments.getText().toString();
        String location = mLocation.getText().toString();

        RCApiManager.provision(publicKey, signature, challenge, name, comments, owner, timestamp, location,new Callback<RCApiResponse>() {
            @Override
            public void onResponse(Call<RCApiResponse> call, Response<RCApiResponse> response) {
                RCApiResponse body = response.body();
                onTwinCreatedSuccessfully();
            }

            @Override
            public void onFailure(Call<RCApiResponse> call, Throwable t) {

            }
        });
    }

    private void onTwinCreatedSuccessfully(){
        Intent intent = new Intent(CreateDigitalTwinActivity.this,TwinCreatedActivity.class);
        startActivity(intent);
        finish();
    }

    public String getDateFromDatePicker() {
        int day = mDatePicker.getDayOfMonth();
        int month = mDatePicker.getMonth();
        int year = mDatePicker.getYear();

        return day + "/" + month + "/" + year;
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int fineLocationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (fineLocationPermissionCheck != PackageManager.PERMISSION_GRANTED && coarseLocationPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                  this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean permissionsGranted = true;
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length == permissions.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        permissionsGranted = false;
                        break;
                    }
                }
                if (permissionsGranted) {
                    try {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    } catch (SecurityException e) {
                        //Do nothing
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private String getLocationName(){
        String city = "";
        if(mLastLocation != null){
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                if (!addresses.isEmpty()) {
                    city = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return city;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
