package com.fast.ilumer.gank.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fast.ilumer.gank.App;

import java.io.File;
import java.util.concurrent.TimeUnit;

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
    private static volatile OkHttpClient client;
    private static volatile Retrofit retrofit;
    private static volatile RetrofitHelper helper;

    private RetrofitHelper(){
        initClient();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .client(getClientInstance())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitHelper getRetrofitInstance(){
        if (helper==null){
            synchronized (RetrofitHelper.class){
                if (helper==null){
                    helper = new RetrofitHelper();
                }
            }
        }
        return helper;
    }

    public Gank getGank(){
        return retrofit.create(Gank.class);
    }

    public static OkHttpClient getClientInstance(){
        if (client==null){
            synchronized (RetrofitHelper.class){
                if (client==null){
                    client = initClient();
                }
            }
        }
        return client;
    }

    private static OkHttpClient initClient(){
        OkHttpClient initClient;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        int cacheSize = 10*10*1024;
        Cache cache = new Cache(new File(App.getAppContext().getCacheDir(),"httpCache"),cacheSize);
        initClient = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(600, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(interceptor)
                .build();
        return initClient;
    }
}
