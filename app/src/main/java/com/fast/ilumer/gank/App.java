package com.fast.ilumer.gank;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.stetho.Stetho;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 11/17/16.
 */

public class App extends Application {

    static RefWatcher refWatcher;
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, RetrofitHelper.initClient())
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setRequestListeners(requestListeners)
                .build();
        Fresco.initialize(this, config);
        Stetho.initializeWithDefaults(this);
        FLog.setMinimumLoggingLevel(FLog.ERROR);
        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        refWatcher=LeakCanary.install(this);
    }

    public static Context getAppContext(){
        return App.context;
    }

    public static RefWatcher getRefWatcher(){
        return refWatcher;
    }
}
