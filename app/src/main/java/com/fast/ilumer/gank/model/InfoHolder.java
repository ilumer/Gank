package com.fast.ilumer.gank.model;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.ilumer.gank.R;
import com.rd.PageIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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
    RelativeLayout bottomBackground;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    @BindString(R.string.none)
    String none;
    @BindColor(R.color.transparent_dark)
    int transDarkColor;
    @BindColor(android.R.color.white)
    int whiteColor;
    @BindColor(android.R.color.black)
    int blackColor;

    Subscription subscription;
    public InfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bindModel(GankInfo item){
        if (item.getImages()==null){
            noImageState();
        }else{
            haveImageState(item);
        }
        if (item.getWho()==null){
            mRecommender.setText(none);
        }else {
            mRecommender.setText(item.getWho());
        }
        mDesc.setText(item.getDesc());
        mPublishDate.setText(formatDate(item.getPublishedAt()));
    }

    private void startPlay(){
        subscription = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    int currentPosition = imageViewPager.getCurrentItem();
                    currentPosition++;
                    imageViewPager.setCurrentItem(currentPosition==imageViewPager.getAdapter().getCount()?0:currentPosition);
                });
    }

    private void stopPlay(){
        if (subscription!=null&&!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void noImageState(){
        imageViewPager.setVisibility(View.GONE);
        bottomBackground.setBackgroundColor(whiteColor);
        mPublishDate.setTextColor(blackColor);
        mDesc.setTextColor(blackColor);
        mDesc.setMaxLines(Integer.MAX_VALUE);
        mRecommender.setTextColor(blackColor);
    }

    private void haveImageState(GankInfo gankInfo){
        imageViewPager.setVisibility(View.VISIBLE);
        imageViewPager.setAdapter(new PicPagerAdapter(gankInfo.getImages()));

        mPublishDate.setTextColor(whiteColor);
        mDesc.setTextColor(whiteColor);
        mRecommender.setTextColor(whiteColor);

        mDesc.setMaxLines(1);
        bottomBackground.setBackgroundColor(transDarkColor);
        if (gankInfo.getImages().size()>1){
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
            pageIndicatorView.setCount(imageViewPager.getAdapter().getCount());
            startPlay();
        }else {
            pageIndicatorView.setVisibility(View.GONE);
        }

    }


    private static CharSequence formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dateFormat.format(date);
    }
}
