package com.fast.ilumer.gank.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fast.ilumer.gank.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/26/16.
 *
 */

public class InfoHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.desc)
    TextView mDesc;
    @BindView(R.id.recommender)
    TextView mRecommender;
    @BindView(R.id.tag)
    TextView mTag;
    public InfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void BindModel(GankInfo item, boolean Istag){
        if (Istag) {
            mTag.setVisibility(View.VISIBLE);
            mTag.setText(item.getType());
        }
        mDesc.setText(item.getDesc());
        mRecommender.setText(item.getWho());
    }

}
