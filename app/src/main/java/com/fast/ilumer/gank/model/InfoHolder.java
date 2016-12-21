package com.fast.ilumer.gank.model;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fast.ilumer.gank.R;
import com.rd.PageIndicatorView;

import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by root on 9/26/16.
 *
 */

public class InfoHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.recommender)
    TextView mRecommender;
    @BindView(R.id.description)
    TextView mDesc;
    @BindView(R.id.publishdate)
    TextView mPublishDate;
    @BindView(R.id.imageViewPager)
    ViewPager imageViewPager;
    @BindView(R.id.bottom_background)
    LinearLayout bottomBackground;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    @BindString(R.string.none)
    String none;
    Subscription subscription;
    public InfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bindModel(GankInfo item, Context context){
        if (item.getImages()==null){
            imageViewPager.setVisibility(View.GONE);
        }else{
            imageViewPager.setVisibility(View.VISIBLE);
            imageViewPager.setAdapter(new ImagePageAdapter(item.getImages()));
            imageViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:{
                            stopPlay();
                            break;
                        }
                        case MotionEvent.ACTION_UP:{
                            startPlay();
                            break;
                        }
                    }
                    return false;
                }
            });
            pageIndicatorView.setViewPager(imageViewPager);
            //pageIndicatorView.setSelection(imageViewPager.getCurrentItem());
            pageIndicatorView.setCount(imageViewPager.getAdapter().getCount());
            mDesc.setMaxLines(1);
            startPlay();
        }
        if (item.getWho()==null){
            mRecommender.setText(none);
        }else {
            mRecommender.setText(item.getWho());
        }
        mDesc.setText(item.getDesc());
        mPublishDate.setText(item.getPublishedAt().toString());
    }

    private void startPlay(){
        subscription = Observable.interval(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        int currentPosition = imageViewPager.getCurrentItem();
                        currentPosition++;
                        imageViewPager.setCurrentItem(currentPosition==imageViewPager.getAdapter().getCount()?0:currentPosition);
                    }
                });
    }

    private void stopPlay(){
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
