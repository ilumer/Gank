package com.fast.ilumer.gank.activity;

import android.support.v4.app.Fragment;

import com.fast.ilumer.gank.fragment.GankTypeFragment;

/**
 * Created by ${ilumer} on 2/2/17.
 */

public class SearchResultActivity extends BaseActivity {
    public static final String EXTRA_STRING = "SearchResultActivity.Type";

    @Override
    public Fragment createNewFragment() {
        String type = getIntent().getStringExtra(EXTRA_STRING);
        return GankTypeFragment.newInstance(type);
    }
}
