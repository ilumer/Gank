package com.fast.ilumer.gank;

import android.app.Application;
import android.content.Context;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.stetho.Stetho;
import com.hugo.watcher.Watcher;
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
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setRequestListeners(requestListeners)
                .build();
        Fresco.initialize(this, config);
        Stetho.initializeWithDefaults(this);
        FLog.setMinimumLoggingLevel(FLog.ERROR);
        context = getApplicationContext();
        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        refWatcher=LeakCanary.install(this);
        Watcher.getInstance().start(this);
    }

    public static Context getAppContext(){
        return App.context;
    }

    public static RefWatcher getRefWatcher(){
        return refWatcher;
    }
}
