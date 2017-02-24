package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.fragment.PictureFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureActivity extends BaseActivity{
    public static final String INTENT_EXTRA_URL = "PictureActivity.extra_url";

    @Override
    public Fragment createNewFragment() {
        String url = getIntent().getStringExtra(INTENT_EXTRA_URL);
        return PictureFragment.newInstance(url);
    }
}
