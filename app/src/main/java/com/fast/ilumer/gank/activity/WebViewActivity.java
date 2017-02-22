package com.fast.ilumer.gank.activity;

import android.support.v4.app.Fragment;

import com.fast.ilumer.gank.fragment.WebViewFragment;

/**
 * Created by ilumer on 17-2-22.
 */

public class WebViewActivity extends BaseActivity {
    public static final String EXTRA_INTENT_URL = "WebViewActivity.extra_intent_uri";

    @Override
    public Fragment createNewFragment() {
        String uri = getIntent().getStringExtra(EXTRA_INTENT_URL);
        return WebViewFragment.newInstance(uri);
    }
}
