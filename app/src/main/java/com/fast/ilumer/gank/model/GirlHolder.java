package com.fast.ilumer.gank.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 12/24/16.
 */

public class GirlHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.gril)
    SimpleDraweeView imageView;
    public GirlHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
