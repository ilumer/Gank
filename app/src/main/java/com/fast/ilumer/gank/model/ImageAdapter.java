package com.fast.ilumer.gank.model;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.activity.PictureActivity;
import com.fast.ilumer.gank.widget.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 10/22/16.
 *
 */

public class ImageAdapter extends ProgressAdapter{

    private Fragment host;

    public ImageAdapter(List<GankInfo> mContentList,Fragment fragment) {
        super(mContentList);
        this.host = fragment;
    }

    @Override
    RecyclerView.ViewHolder getInfoViewHolder(ViewGroup parent) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gril_viewholder,parent,false));
    }

    @Override
    void BindInfoViewHolder(GankInfo info, RecyclerView.ViewHolder holder) {
        Glide.with(host)
                .load(info.getUrl())
                .centerCrop()
                //等同于scaletype中的center_crop
                //按宽高部分进行缩放多余部分裁取
                .into(((ImageViewHolder)holder).imageView);
        ((ImageViewHolder)holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(host.getActivity(), PictureActivity.class);
                host.getActivity().startActivity(i);
            }
        });
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.gril)
        RatioImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            imageView.setRatio(0.618f);
        }
    }
}
