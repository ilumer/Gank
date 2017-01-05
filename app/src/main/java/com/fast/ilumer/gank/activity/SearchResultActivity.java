package com.fast.ilumer.gank.activity;

import android.support.v4.app.Fragment;

import com.fast.ilumer.gank.fragment.GankTypeFragment;

public class SearchResultActivity extends BaseActivity {

    @Override
    public Fragment createNewFragment() {
        return GankTypeFragment.newInstance("福利");
    }
}
