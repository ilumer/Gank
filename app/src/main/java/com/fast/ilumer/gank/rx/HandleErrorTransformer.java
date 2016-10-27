package com.fast.ilumer.gank.rx;

import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.network.Gank;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by root on 10/17/16.
 *
 */

public class HandleErrorTransformer implements Observable.Transformer<Gank.Result<List<GankInfo>>,List<GankInfo>> {
    @Override
    public Observable<List<GankInfo>> call(Observable<Gank.Result<List<GankInfo>>> resultObservable) {
        return resultObservable.flatMap(new Func1<Gank.Result<List<GankInfo>>, Observable<List<GankInfo>>>() {
            @Override
            public Observable<List<GankInfo>> call(Gank.Result<List<GankInfo>> listResult) {
                return RxUtil.flatResult(listResult);
            }
        });
    }
}
