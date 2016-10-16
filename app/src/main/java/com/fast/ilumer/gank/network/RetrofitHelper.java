package com.fast.ilumer.gank.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 9/26/16.
 *
 */

public class RetrofitHelper {
    private Retrofit retrofit;

    private RetrofitHelper(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        .build();
    }

    private static class SingleonHolder{
        private static RetrofitHelper Instance = new RetrofitHelper();
    }

    public static RetrofitHelper getInstance(){
        return SingleonHolder.Instance;
    }

    public  Gank getGankDaily(){
        return retrofit.create(Gank.class);
    }


}
