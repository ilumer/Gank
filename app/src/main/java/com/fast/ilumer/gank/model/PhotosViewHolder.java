package com.fast.ilumer.gank.model;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fast.ilumer.gank.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/26/16.
 *
 */

public class PhotosViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tag)
    TextView mTag;
    @BindView(R.id.MEIZI)
    ImageView mImageView;
    public PhotosViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void BindModel(GankInfo item, boolean Istag, Fragment fragment){
        if (Istag) {
            mTag.setVisibility(View.VISIBLE);
            mTag.setText(item.getType());
        }
        Glide.with(fragment)
                .load(item.getUrl())
                .into(mImageView);

    }
}
