package com.fast.ilumer.gank.model.viewholder;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.PicPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 12/26/16.
 */

public class InfoPicHolder extends InfoHolder{
    @BindView(R.id.imageViewPager)
    ViewPager picViewPager;

    public InfoPicHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindModel(GankInfo item) {
        super.bindModel(item);
        picViewPager.setAdapter(new PicPagerAdapter(item.getImages()));
    }
}
