package com.fast.ilumer.gank.rx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Subscription;

/**
 * Created by ilumer on 17-2-20.
 */

public final class SubscriptionManager {
    volatile static SubscriptionManager instance;
    //用唯一的gank uri
    private Map<String, Subscription> map = new ConcurrentHashMap<>();

    private SubscriptionManager(){

    }

    public static SubscriptionManager get(){
        if (instance==null) {
            synchronized (SubscriptionManager.class) {
                if (instance == null) {
                    instance = new SubscriptionManager();
                }
            }
        }
        return instance;
    }

    public void cancel(){
        for (String key:map.keySet()){
            Subscription subscription = map.get(key);
            if (!subscription.isUnsubscribed()){
                subscription.unsubscribe();
            }
        }
        map.clear();
    }

    public void put(String urlId,Subscription subscription){
        map.put(urlId,subscription);
    }
}
