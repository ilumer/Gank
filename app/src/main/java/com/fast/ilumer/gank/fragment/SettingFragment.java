package com.fast.ilumer.gank.fragment;

import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.Preference;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fast.ilumer.gank.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilumer on 17-2-21.
 *
 */

public class SettingFragment extends PreferenceFragmentCompat {
    CompositeSubscription subscription = null;
    Preference deleteCache;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_preference);
        deleteCache = findPreference(getString(R.string.delete));
        showCache();
        subscription = new CompositeSubscription();
        deleteCache.setOnPreferenceClickListener(preference -> {
            clearDiskCache();
                subscription.add(Observable.timer(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                           showCache();
                        }));
                return true;
            }
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
            subscription.unsubscribe();
    }

    public int getImageDiskCache(){
        long bytes = Fresco.getImagePipelineFactory().getMainFileCache().getSize();
        return bytes==-1?0:Math.round(bytes/1024);
    }

    public void showCache(){
        deleteCache.setSummary(String.format(getString(R.string.delete_summary),getImageDiskCache()));
    }

    public void clearDiskCache(){
        Fresco.getImagePipeline().clearDiskCaches();
    }
}
