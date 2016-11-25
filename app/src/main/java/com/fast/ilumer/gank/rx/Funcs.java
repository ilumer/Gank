package com.fast.ilumer.gank.rx;

import rx.functions.Func1;

/**
 * Created by root on 11/25/16.
 */

public final class Funcs {
    public static <T>Func1<T,Boolean> not (final Func1<T,Boolean> func){
            return new Func1<T, Boolean>() {
                @Override
                public Boolean call(T t) {
                    return !func.call(t);
                }
            };
        }

    public Funcs() {
        throw new AssertionError("error");
    }
}
