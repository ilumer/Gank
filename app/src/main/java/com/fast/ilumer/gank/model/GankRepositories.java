package com.fast.ilumer.gank.model;

/**
 * Created by root on 11/25/16.
 */

public class GankRepositories<T> {
    public final T results;

    public GankRepositories(T items) {
        this.results = items;
    }
}
