package com.fast.ilumer.gank.model;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.activity.PictureActivity;
import com.fast.ilumer.gank.model.viewholder.GirlHolder;

import java.util.List;

/**
 * Created by root on 10/22/16.
 *
 */

public class GirlAdapter extends ProgressAdapter{
    public static final int GANK_VIEW_GIRL = 1;
    private Fragment host;

    public GirlAdapter(List<GankInfo> mContentList, Fragment fragment) {
        super(mContentList);
        this.host = fragment;
    }

    @Override
    public int getItemViewTypeExt(int position) {
        return GANK_VIEW_GIRL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateExtViewHolder(ViewGroup parent, int viewType) {
        return new GirlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gank_girl_pic,parent,false));
    }

    @Override
    public void onBindExtViewHolder(RecyclerView.ViewHolder holder, GankInfo info) {
        ((GirlHolder)holder).setAspectRatio(0.618f);
        ((GirlHolder)holder).bindModel(info);
        ((GirlHolder)holder).imageView.setOnClickListener(v->{
            Intent i = new Intent(host.getActivity(), PictureActivity.class);
            i.putExtra("uri",info.getUrl());
            host.getActivity().startActivity(i);
        });
    }
}