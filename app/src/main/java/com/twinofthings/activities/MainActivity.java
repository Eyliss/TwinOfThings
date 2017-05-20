package com.twinofthings.activities;

import com.twinofthings.R;
import com.twinofthings.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button mCreateTwinButton;
    private Button mScanTagButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreateTwinButton = (Button)findViewById(R.id.create_twin_button);
        mCreateTwinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextScreen(Constants.CREATE_TWIN);
            }
        });
        mScanTagButton = (Button)findViewById(R.id.scan_tag_button);
        mScanTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextScreen(Constants.SCAN);
            }
        });
    }

    private void goToNextScreen(String proccess){
        Intent intent = new Intent(getApplicationContext(), ReaderActivity.class);
        intent.putExtra(Constants.INTENT_PROCESS_TYPE,proccess);
        startActivity(intent);
    }
}
