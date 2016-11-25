package com.fast.ilumer.gank.network;

import com.fast.ilumer.gank.model.GankDaily;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;

/**
 * Created by root on 11/25/16.
 */

public final class Results {
    private static final Func1<Result<?>,Boolean> SUCCESSFUL = new Func1<Result<?>, Boolean>() {
        @Override
        public Boolean call(Result<?> result) {
            return !result.isError()&&result.response().isSuccessful();
        }
    };

    private static final Func1<GankDaily,Boolean> ISNONE = new Func1<GankDaily, Boolean>() {
        @Override
        public Boolean call(GankDaily gankDaily) {
            return gankDaily.Android!=null&&gankDaily.iOS!=null;
            //这两个标志位代表是否为空
        }
    };
    public static Func1<Result<?>,Boolean> isSuccessful(){
        return SUCCESSFUL;
    }

    public static Func1<GankDaily,Boolean> ISNULL(){
        return ISNONE;
    }

    private Results(){
        throw new AssertionError("error");
    }
}
