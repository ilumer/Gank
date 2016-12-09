package com.fast.ilumer.gank.model;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.activity.PictureActivity;

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
    void BindInfoViewHolder(final GankInfo info, RecyclerView.ViewHolder holder) {
        ((ImageViewHolder)holder).imageView.
                setImageURI(info.getUrl());
        ((ImageViewHolder)holder).imageView.setOnClickListener(v -> {
            Intent i = new Intent(host.getActivity(), PictureActivity.class);
            i.putExtra("uri",info.getUrl());
            host.getActivity().startActivity(i);
        });
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.gril)
        SimpleDraweeView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
