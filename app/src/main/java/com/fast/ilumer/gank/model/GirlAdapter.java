package com.fast.ilumer.gank.model;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.activity.PictureActivity;
import com.fast.ilumer.gank.model.viewholder.BaseHolder;
import com.fast.ilumer.gank.model.viewholder.GirlHolder;

import java.util.List;

/**
 * Created by root on 10/22/16.
 *
 */

public class GirlAdapter extends ProgressAdapter{
    public static final int GANK_VIEW_GIRL = 4;
    private Activity host;

    public GirlAdapter(List<GankInfo> mContentList, Activity activity) {
        super(mContentList);
        this.host = activity;
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
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof BaseHolder){
            ((BaseHolder) holder).onViewRecycled();
        }
    }

    @Override
    public void onBindExtViewHolder(RecyclerView.ViewHolder holder, GankInfo info) {
        ((GirlHolder)holder).setAspectRatio(0.618f);
        ((GirlHolder)holder).bindModel(info);
        ((GirlHolder)holder).imageView.setOnClickListener(v->{
            Intent i = new Intent(host, PictureActivity.class);
            i.putExtra(PictureActivity.INTENT_EXTRA_URL,info.getUrl());
            host.startActivity(i);
        });
    }
}