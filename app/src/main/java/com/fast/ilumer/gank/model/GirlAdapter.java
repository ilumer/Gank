package com.fast.ilumer.gank.model;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.activity.PictureActivity;

import java.util.List;

/**
 * Created by root on 10/22/16.
 *
 */

public class GirlAdapter extends ProgressAdapter{

    private Fragment host;

    public GirlAdapter(List<GankInfo> mContentList, Fragment fragment) {
        super(mContentList);
        this.host = fragment;
    }

    @Override
    RecyclerView.ViewHolder getInfoViewHolder(ViewGroup parent) {
        return new GirlHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gank_girl_pic,parent,false));
    }

    @Override
    void BindInfoViewHolder(final GankInfo info, RecyclerView.ViewHolder holder) {
        ((GirlHolder)holder).imageView.setAspectRatio(0.618f);
        ((GirlHolder)holder).imageView.
                setImageURI(info.getUrl());
        ((GirlHolder)holder).imageView.setOnClickListener(v -> {
            Intent i = new Intent(host.getActivity(), PictureActivity.class);
            i.putExtra("uri",info.getUrl());
            host.getActivity().startActivity(i);
        });
    }
}
