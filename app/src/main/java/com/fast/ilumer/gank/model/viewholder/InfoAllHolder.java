package com.fast.ilumer.gank.model.viewholder;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.PicPagerAdapter;
import com.rd.PageIndicatorView;

import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by root on 12/26/16.
 */

public class InfoAllHolder extends InfoHolder {
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView mIndicatorView;
    @BindView(R.id.imageViewPager)
    ViewPager picViewPager;
    @BindColor(R.color.transparent_dark)
    int transparentDark;
    @BindColor(android.R.color.white)
    int white;
    PicPagerAdapter picPagerAdapter;
    Subscription subscription = null;

    public InfoAllHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        bottomBackground.setBackgroundColor(transparentDark);
        mDesc.setTextColor(white);
        mDesc.setMaxLines(1);
        mRecommender.setTextColor(white);
        mPublishDate.setTextColor(white);
    }

    @Override
    public void bindModel(GankInfo item) {
        super.bindModel(item);
        picViewPager.setOnTouchListener(((v, event) -> {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:{
                    stopPlay();
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:{
                    startPlay();
                    break;
                }
            }
            return false;
        }));
        picPagerAdapter = new PicPagerAdapter(item.getImages());
        picViewPager.setAdapter(picPagerAdapter);
        mIndicatorView.setViewPager(picViewPager);
        mIndicatorView.setCount(picViewPager.getAdapter().getCount());
        mIndicatorView.setSelection(picViewPager.getCurrentItem());
    }

    @Override
    public void onAttach() {
        super.onAttach();
        startPlay();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stopPlay();
    }

    private void stopPlay(){
        if (subscription!=null) {
            subscription.unsubscribe();
        }
    }

    private void startPlay(){
        if (subscription==null || subscription.isUnsubscribed()){
            subscription =
                    Observable.interval(3, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                int currentPosition = picViewPager.getCurrentItem();
                                currentPosition++;
                                picViewPager.setCurrentItem(currentPosition == picViewPager.getAdapter().getCount() ? 0 : currentPosition);
                            });
        }
    }
}
