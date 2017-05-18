package com.twinofthings.activities;

import android.os.Bundle;
import android.app.Activity;

import com.twinofthings.R;

public class CreateDigitalTwinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_digital_twin);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
