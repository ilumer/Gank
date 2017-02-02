package com.fast.ilumer.gank.activity;

import android.support.v4.app.Fragment;

import com.fast.ilumer.gank.fragment.SearchResultFragment;

/**
 * Created by ${ilumer} on 2/2/17.
 */

public class SearchResultActivity extends BaseActivity {


    @Override
    public Fragment createNewFragment() {
        return new SearchResultFragment();
    }
}
