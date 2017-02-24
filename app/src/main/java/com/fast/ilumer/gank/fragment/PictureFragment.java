package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;

import butterknife.BindView;

/**
 * Created by ilumer on 17-2-24.
 */

public class PictureFragment extends BaseFragment {
    public static final String EXTRA_URL = "PictureFragment.extra_url";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    SimpleDraweeView simpleDraweeView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Gank");
        String url = getArguments().getString(EXTRA_URL);
        simpleDraweeView.setImageURI(url);
    }


    public static PictureFragment newInstance(String url){
        Bundle arg = new Bundle();
        arg.putString(EXTRA_URL,url);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture;
    }
}
