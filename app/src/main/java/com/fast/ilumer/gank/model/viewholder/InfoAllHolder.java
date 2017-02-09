package com.fast.ilumer.gank.model.viewholder;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.PicPagerAdapter;
import com.rd.PageIndicatorView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by root on 12/26/16.
 */

public class InfoAllHolder extends InfoPicHolder {
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView mIndicatorView;
    @BindView(R.id.imageViewPager)
    ViewPager picViewPager;
    Subscription subscription;

    public InfoAllHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
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
        picViewPager.setAdapter(new PicPagerAdapter(item.getImages()));
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
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void startPlay(){
        subscription = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    int currentPosition = picViewPager.getCurrentItem();
                    currentPosition++;
                    picViewPager.setCurrentItem(currentPosition==picViewPager.getAdapter().getCount()?0:currentPosition);
                });
    }
}
