package com.twinofthings.activities;

import com.google.gson.Gson;

import com.twinofthings.R;
import com.twinofthings.api.RCApiManager;
import com.twinofthings.api.RCApiResponse;
import com.twinofthings.fragments.ScanFragment;
import com.twinofthings.models.Credentials;
import com.twinofthings.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private Button mCreateTwinButton;
    private Button mScanTagButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
    }

    private void bindViews(){
        mCreateTwinButton = (Button)findViewById(R.id.create_twin_button);
        mCreateTwinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCredentials(Constants.CREATE_TWIN);
            }
        });
        mScanTagButton = (Button)findViewById(R.id.scan_tag_button);
        mScanTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReaderScreen(Constants.SCAN,null);
            }
        });
    }

    private void getCredentials(final String proccess){

        RCApiManager.getCredentials(new Callback<RCApiResponse>() {
            @Override
            public void onResponse(Call<RCApiResponse> call, Response<RCApiResponse> response) {
                RCApiResponse apiResponse = response.body();

                if(apiResponse.isSuccessful()){
                    Gson gson = new Gson();
                    Credentials credentials = gson.fromJson(apiResponse.getStringData(), Credentials.class);
                    goToReaderScreen(proccess,credentials);
                }else{
                    Toast.makeText(MainActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RCApiResponse> call, Throwable t) {

            }
        });
    }

    private void goToReaderScreen(String proccess, Credentials credentials){
        Intent intent = new Intent(getApplicationContext(), ReaderActivity.class);
        intent.putExtra(Constants.PUB_KEY, credentials != null ? credentials.getPublicKey() : "");
        intent.putExtra(Constants.SIGNATURE,credentials != null ? credentials.getSignature() : "");
        intent.putExtra(Constants.CHALLENGE,credentials != null ? credentials.getChallenge() : "");
        intent.putExtra(Constants.INTENT_PROCESS_TYPE,proccess);
        startActivity(intent);
    }
}
