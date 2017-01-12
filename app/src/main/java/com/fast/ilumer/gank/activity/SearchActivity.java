package com.fast.ilumer.gank.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.fast.ilumer.gank.fragment.SearchFragment;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    public Fragment createNewFragment() {
        return new SearchFragment();
    }
}
