package com.fast.ilumer.gank.network;

import com.fast.ilumer.gank.model.DailyData;
import com.fast.ilumer.gank.model.GankInfo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by root on 9/25/16.
 * 网络访问
 */

public interface Gank {
    @GET("day/{year}/{month}/{day}")
    Observable<Result<DailyData>> GankDailyInfo(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("data/{type}/{number}/{page}")
    Observable<Result<List<GankInfo>>> GankTypeInfo(@Path("type") String type, @Path("number") int number, @Path("page") int page);

    class Result<T>{
        public boolean error;
        public T results;
        //Gson解析时对象的命名需要相同
    }
}
