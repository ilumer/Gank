package com.fast.ilumer.gank.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fast.ilumer.gank.App;

import java.io.File;

import okhttp3.Cache;
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
    private OkHttpClient client;
    private Retrofit retrofit;

    private RetrofitHelper(){
        initClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        .build();
    }

    public static RetrofitHelper getInstance(){
        return LazyHelper.INSTANCE;
    }

    public Gank getGank(){
        return retrofit.create(Gank.class);
    }

    private void initClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        int cacheSize = 10*10*1024;
        Cache cache = new Cache(new File(App.getAppContext().getCacheDir(),"httpCache"),cacheSize);
        client = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(interceptor)
                .build();
    }

    private static class LazyHelper{
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }


}
