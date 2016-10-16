package com.fast.ilumer.gank.rx;

import com.fast.ilumer.gank.network.Gank;
import com.fast.ilumer.gank.network.NewWorkException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by root on 10/15/16.
 */

public class RxUtil {
    public static <T> Observable<T> flatResult(final Gank.Result<T> result){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (!result.error){
                    subscriber.onNext(result.results);
                }else {
                    subscriber.onError(new NewWorkException());
                }
                subscriber.onCompleted();
            }
        });
    }
}
