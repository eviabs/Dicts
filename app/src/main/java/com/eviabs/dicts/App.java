package com.eviabs.dicts;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Our App class.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
