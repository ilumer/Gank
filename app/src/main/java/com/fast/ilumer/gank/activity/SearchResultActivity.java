package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.fragment.GankTypeFragment;

/**
 * Created by ${ilumer} on 2/2/17.
 */

public class SearchResultActivity extends BaseActivity {
    public static final String EXTRA_STRING = "SearchResultActivity.Type";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra(EXTRA_STRING));
    }

    @Override
    protected int providerLayoutId() {
        return R.layout.search_result_layout;
    }

    @Override
    public Fragment createNewFragment() {
        String type = getIntent().getStringExtra(EXTRA_STRING);
        return GankTypeFragment.newInstance(type);
    }
}
