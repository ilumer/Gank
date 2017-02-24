package com.fast.ilumer.gank.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.service.DownloadImgService;
import com.fast.ilumer.gank.service.MuzeiSource;

import butterknife.BindView;

/**
 * Created by ilumer on 17-2-24.
 */

public class PictureFragment extends BaseFragment {
    public static final String EXTRA_URL = "PictureFragment.extra_url";
    String url;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    SimpleDraweeView simpleDraweeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.download:{
                Intent intent = new Intent(getActivity(), DownloadImgService.class);
                intent.putExtra(EXTRA_URL,url);
                getActivity().startService(intent);
                break;
            }
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.picture_menu, menu);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Gank");
        url = getArguments().getString(EXTRA_URL);
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
