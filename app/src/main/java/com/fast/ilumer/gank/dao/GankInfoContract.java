package com.fast.ilumer.gank.dao;

import android.provider.BaseColumns;

/**
 * Created by root on 11/26/16.
 * database schema
 */

public final class GankInfoContract {

    public GankInfoContract() {
        throw new AssertionError("no instance");
    }

    public static class GankEntry implements BaseColumns{
        public static final String URL_ID = "url_id";
        //用来比较eqauls原本就继承了id 映射的是gank api中的
        public static final String URL = "url";
        public static final String IMAGE = "image";
        public static final String PUBLISHRD_AT ="published_at";
        public static final String CREATED_AT = "created_at";
        public static final String WHO = "who";
        public static final String DESC = "description";
        public static final String TYPE = "type";
        public static final String USED = "used";
        //sqlite中只有四种数据类型
    }
}
