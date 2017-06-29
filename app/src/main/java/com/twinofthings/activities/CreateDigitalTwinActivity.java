package com.twinofthings.activities;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.twinofthings.R;
import com.twinofthings.api.RCApiManager;
import com.twinofthings.api.RCApiResponse;
import com.twinofthings.helpers.CircleImageView;
import com.twinofthings.utils.Constants;
import com.twinofthings.utils.Util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDigitalTwinActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DatePickerDialog.OnDateSetListener {

    public static final int REQUEST_LOCATION_PERMISSION = 0x17;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String[] CAMERA_PERMISSIONS = {
          Manifest.permission.CAMERA,
          Manifest.permission.WRITE_EXTERNAL_STORAGE
    };;

    private String publicKey;
    private String challenge;
    private String signature;

    private EditText mBrandName;
    private TextView mTimestamp;
    private DatePickerDialog mDatePickerDialog;
    private EditText mProductName;
    private EditText mProductSubline;
    private EditText mOwnerName;
    private EditText mSerialId;
    private EditText mMaterial;
    private EditText mComments;

//    private TextView mLocation;
    private Button mCreateTwin;
    private CircleImageView mUploadPicture;

    private Location mLastLocation;
    private String locationName;
    private int day;
    private int month;
    private int year;
    private Bitmap thumbnailBitmap;
    private String thumbnailEncoded;

    private GoogleApiClient mGoogleApiClient;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_digital_twin);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        publicKey = getIntent().getStringExtra(Constants.PUB_KEY);
        signature = getIntent().getStringExtra(Constants.SIGNATURE);
        challenge = getIntent().getStringExtra(Constants.CHALLENGE);

        setupGoogleApliClient();

        setupDatePicker();

        bindViews();

//        setDeviceCurrentLocation();

    }

    //Instantiate the google api client and activate location services API
    private void setupGoogleApliClient(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                  .addConnectionCallbacks(this)
                  .addOnConnectionFailedListener(this)
                  .addApi(LocationServices.API)
                  .build();
        }
    }

    //Creates a date picker for get timestamp which is activated when the text field is clicked
    private void setupDatePicker(){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        mDatePickerDialog = new DatePickerDialog (this,this,year,month,day);
    }

    private void bindViews(){

        mBrandName = (EditText) findViewById(R.id.brand_name);
        mProductName = (EditText) findViewById(R.id.product_name);
        mProductSubline = (EditText) findViewById(R.id.product_subline);
        mOwnerName = (EditText) findViewById(R.id.owner_name);
        mSerialId = (EditText) findViewById(R.id.serial_id);
        mMaterial = (EditText) findViewById(R.id.material);
        mComments = (EditText) findViewById(R.id.comments);

//        mLocation = (TextView) findViewById(R.id.twin_location);
        mTimestamp = (TextView) findViewById(R.id.tv_timestamp);
        mTimestamp.setText(getStringDate());
        mTimestamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        mCreateTwin = (Button) findViewById(R.id.create_digital_twin_button);
        mCreateTwin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTwinDataToServer();
            }
        });

        mUploadPicture = (CircleImageView) findViewById(R.id.ib_upload_picture);
        mUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermissions();
            }
        });
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void requestCameraPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            dispatchTakePictureIntent();
        }else{
            if (!shouldShowRequestPermissionRationale(CAMERA_PERMISSIONS)) {
                dispatchTakePictureIntent();
            } else {
                requestPermissions(CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSION);
            }
        }

    }

    /**
     * Gets whether you should show UI with rationale for requesting permissions.
     *
     * @param permissions The permissions your app wants to request.
     * @return Whether you can show permission rationale UI.
     */
    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            final int numOfRequest = grantResults.length;
            final boolean isGranted = numOfRequest == 1
                  && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
            if (isGranted) {
                dispatchTakePictureIntent();
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            boolean permissionsGranted = true;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            thumbnailBitmap = (Bitmap) extras.get("data");
            mUploadPicture.setImageBitmap(thumbnailBitmap);
        }
    }

//    private void setDeviceCurrentLocation(){
//        locationName = getLocationName();
//        mLocation.setText(locationName);
//    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private void sendTwinDataToServer() {

        String brandName = mBrandName.getText().toString();
        String timestamp = getStringDate();
        String productName = mProductName.getText().toString();
        String productSubline = mProductSubline.getText().toString();
        String ownerName = mOwnerName.getText().toString();
        String serialId = mSerialId.getText().toString();
        String material = mMaterial.getText().toString();
        String comments = mComments.getText().toString();
//        String location = mLocation.getText().toString();
        thumbnailEncoded = Util.encodeBitmapToBase64(thumbnailBitmap, Bitmap.CompressFormat.PNG);

        RCApiManager.provision(publicKey, signature, challenge, brandName, productName, productSubline,timestamp,ownerName,serialId,material,comments,thumbnailEncoded,new Callback<RCApiResponse>() {
            @Override
            public void onResponse(Call<RCApiResponse> call, Response<RCApiResponse> response) {
                RCApiResponse apiResponse = response.body();
                if(apiResponse.isSuccessful()){
                    Gson gson = new Gson();
                    String transaction = gson.toJson(apiResponse.getData());
                    onTwinCreatedSuccessfully(transaction);
                }
            }

            @Override
            public void onFailure(Call<RCApiResponse> call, Throwable t) {

            }
        });
    }

    private void onTwinCreatedSuccessfully(String transaction){
        Intent intent = new Intent(CreateDigitalTwinActivity.this,TwinCreatedActivity.class);

        intent.putExtra(Constants.INTENT_TRANSACTION,transaction);
        intent.putExtra(Constants.INTENT_IMAGE,thumbnailEncoded);

        intent.putExtra(Constants.PUB_KEY,publicKey);
        intent.putExtra(Constants.SIGNATURE,signature);
        intent.putExtra(Constants.CHALLENGE,challenge);
        startActivity(intent);
        finish();
    }

    public String getStringDate() {

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
    public void onConnectionSuspended(int i) {

    }

    //Use geocoding to get the location name from its latitude and longitude
    private String getLocationName(){
        String city = "";
        if(mLastLocation != null){
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                if (!addresses.isEmpty()) {
                    Log.d("Create Digital Twin",addresses.toString());
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        mTimestamp.setText(getStringDate());
    }
}
