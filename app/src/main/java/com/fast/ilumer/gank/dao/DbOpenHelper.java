package com.fast.ilumer.gank.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fast.ilumer.gank.model.GankInfo;

/**
 * Created by root on 11/13/16.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    private static final String CREATE_GANK =
            "create table" + GankInfo.TABLE +"("
            + GankInfo.ID +"TEXT NOT NULL PRIMARY KEY,"
            +GankInfo.CREATEDAT+"TEXT NOT NULL,"
            +GankInfo.PUBLISHEDAT+"TEXT NOT NULL,"
            +GankInfo.DESC +"TEXT NOT NULL,"
            +GankInfo.URL +"TEXT NOT NULL,"
            +GankInfo.WHO +"TEXT,"
            +GankInfo.USED +"INTEGER,"
            +GankInfo.TYPE +"TEXT NOT NULL."
            +")";




    public DbOpenHelper(Context context) {
        super(context, "gank.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GANK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
