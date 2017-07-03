package com.twinofthings;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Eyliss on 7/3/17.
 */

public class TwinOfThingsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
              .setDefaultFontPath("fonts/CooperHewitt-Book.otf")
              .setFontAttrId(R.attr.fontPath)
              .build()
        );
    }
}
