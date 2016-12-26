package com.fast.ilumer.gank.model.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 12/24/16.
 */

public class GirlHolder extends RecyclerView.ViewHolder implements BindModel {
    @BindView(R.id.gril)
    public SimpleDraweeView imageView;
    private float aspectRatio;
    public GirlHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindModel(GankInfo info) {
        imageView.setImageURI(info.getUrl());
        imageView.setAspectRatio(aspectRatio);
    }

    public void setAspectRatio(float aspectRatio){
        this.aspectRatio = aspectRatio;
    }
}
