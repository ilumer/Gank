package com.fast.ilumer.gank;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by root on 11/17/16.
 */

public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        context = getApplicationContext();
    }

    public static Context getAppContext(){
        return App.context;
    }
}
