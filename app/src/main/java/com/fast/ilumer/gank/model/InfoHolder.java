package com.fast.ilumer.gank.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fast.ilumer.gank.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/26/16.
 *
 */

public class InfoHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.recommender)
    protected TextView mRecommender;
    @BindView(R.id.description)
    protected TextView mDesc;
    @BindString(R.string.none)
    protected String none;
    public InfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void BindModel(GankInfo item){
        if (item.getWho()==null){
            mRecommender.setText(none);
        }else {
            mRecommender.setText(item.getWho());
        }
        mDesc.setText(item.getDesc());
    }

}
