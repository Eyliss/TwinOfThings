package com.twinofthings.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.twinofthings.R;
import com.twinofthings.utils.Constants;

public class TwinCreatedActivity extends Activity {

    private Button mBackButton;
    private Button mScanTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twin_created);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
              .getColor(android.R.color.transparent)));

        mBackButton = (Button)findViewById(R.id.back_to_start);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwinCreatedActivity.this.finish();
            }
        });

        mScanTag = (Button)findViewById(R.id.scan_tag_button);
        mScanTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwinCreatedActivity.this,ReaderActivity.class);
                intent.putExtra(Constants.INTENT_PROCESS_TYPE,Constants.SCAN);
                startActivity(intent);
                finish();
            }
        });
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
