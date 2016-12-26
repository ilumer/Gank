package com.fast.ilumer.gank.model.viewholder;

import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.fast.ilumer.gank.model.GankInfo;
import com.rd.PageIndicatorView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.fast.ilumer.gank.R.id.pageIndicatorView;

/**
 * Created by root on 12/26/16.
 */

public class InfoAllHolder extends InfoPicHolder {
    @Nullable@BindView(pageIndicatorView)
    PageIndicatorView mIndicatorView;
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
        mIndicatorView.setViewPager(picViewPager);
        mIndicatorView.setCount(picViewPager.getAdapter().getCount());
        startPlay();
    }

    private void stopPlay(){
        if (subscription!=null&&!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void startPlay(){
        subscription = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    int currentPosition = picViewPager.getCurrentItem();
                    currentPosition++;
                    picViewPager.setCurrentItem(currentPosition==picViewPager.getAdapter().getCount()?0:currentPosition);
                });
    }
}
