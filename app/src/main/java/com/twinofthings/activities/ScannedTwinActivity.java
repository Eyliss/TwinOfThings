package com.twinofthings.activities;

import android.os.Bundle;
import android.app.Activity;

import com.twinofthings.R;

public class ScannedTwinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_twin);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
