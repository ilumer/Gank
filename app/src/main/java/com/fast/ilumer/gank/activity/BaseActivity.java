package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.fast.ilumer.gank.R;

/**
 * Created by root on 1/4/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutId());
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment==null) {
            fragment = createNewFragment();
            fragmentManager.
                    beginTransaction().
                    add(R.id.fragment_container, fragment).commit();
        }
    }

    public abstract Fragment createNewFragment();

    protected int provideLayoutId(){
        return R.layout.base_activity_layout;
    }
}
