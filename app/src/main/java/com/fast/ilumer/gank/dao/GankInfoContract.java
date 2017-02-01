package com.fast.ilumer.gank.dao;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by root on 11/26/16.
 * database schema
 */

public class GankInfoContract {

    public static String CONTENT_AUTHORITY = "com.fast.ilumer.gank";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public GankInfoContract()    {
        throw new AssertionError("no instance");
    }

    public static final class GankEntry implements BaseColumns{
        public static final String URL_ID = "url_id";
        //用来比较eqauls原本就继承了id 映射的是gank api中的
        public static final String URL = "url";
        public static final String PUBLISHRD_AT ="published_at";
        public static final String CREATED_AT = "created_at";
        public static final String WHO = "who";
        public static final String DESC = "description";
        public static final String TYPE = "type";
        public static final String USED = "used";
        //sqlite中只有四种数据类型
        public static final String IMAGELIST = "images";

        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(Db.TYPE_TABLE_NAME)
                .build();

        public static String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"+Db.TYPE_TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"+Db.TYPE_TABLE_NAME;

        public static Uri buildGankTypeUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }
}
