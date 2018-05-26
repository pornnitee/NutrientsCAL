package com.example.starius.project;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by STARIUS on 3/26/2018.
 */

public class IntialFonts extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/Sukhumvit.ttf")
        .setFontAttrId(R.attr.fontPath)
        .build());
    }
}
