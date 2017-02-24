package com.fast.ilumer.gank.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.fragment.SettingFragment;

public class SettingActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createNewFragment() {
        return new SettingFragment();
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.toolbar_base_layout;
    }
}
