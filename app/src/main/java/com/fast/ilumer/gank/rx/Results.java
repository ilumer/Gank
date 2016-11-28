package com.fast.ilumer.gank.rx;

import com.fast.ilumer.gank.model.GankDaily;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;

/**
 * Created by root on 11/25/16.
 */

public final class Results {
    private static final Func1<Result<?>,Boolean> SUCCESSFUL = (result ->
        !result.isError()&&result.response().isSuccessful()
    );

    private static final Func1<GankDaily,Boolean> NONE = (daily ->
        daily.Android.size()==0&&daily.iOS.size()==0
    );

    public static Func1<Result<?>,Boolean> isSuccessful(){
        return SUCCESSFUL;
    }

    public static Func1<GankDaily,Boolean> isNull(){
        return NONE;
    }

    private Results(){
        throw new AssertionError("error");
    }
}
