package com.fast.ilumer.gank.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.fast.ilumer.gank.dao.Db.TODAY_TABLE_NAME;
import static com.fast.ilumer.gank.dao.Db.TYPE_TABLE_NAME;

/**
 * Created by root on 11/26/16.
 * create table
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String SQL_CREATE_GANKDAILY = "create table if not exists "+TODAY_TABLE_NAME+" ( "
            +GankInfoContract.GankEntry._ID + " Integer primary key,"
            +GankInfoContract.GankEntry.URL_ID + " TEXT , "
            +GankInfoContract.GankEntry.URL + " TEXT , "
            +GankInfoContract.GankEntry.CREATED_AT + " TEXT ,"
            +GankInfoContract.GankEntry.PUBLISHRD_AT + " TEXT ,"
            +GankInfoContract.GankEntry.DESC + " TEXT, "
            +GankInfoContract.GankEntry.WHO + " TEXT, "
            +GankInfoContract.GankEntry.USED + " Interger, "
            +GankInfoContract.GankEntry.IMAGELIST+ " TEXT, "
            +GankInfoContract.GankEntry.TYPE + " TEXT "
            + ")";

    public static final String SQL_CREATE_GANKTYPE = "create table if not exists " + TYPE_TABLE_NAME + " ( "
            +GankInfoContract.GankEntry._ID + " Integer primary key,"
            +GankInfoContract.GankEntry.URL_ID + " TEXT , "
            +GankInfoContract.GankEntry.URL + " TEXT , "
            +GankInfoContract.GankEntry.CREATED_AT + " TEXT ,"
            +GankInfoContract.GankEntry.PUBLISHRD_AT + " TEXT ,"
            +GankInfoContract.GankEntry.DESC + " TEXT, "
            +GankInfoContract.GankEntry.WHO + " TEXT, "
            +GankInfoContract.GankEntry.USED + " Interger, "
            +GankInfoContract.GankEntry.TYPE + " TEXT, "
            +GankInfoContract.GankEntry.IMAGELIST+ " TEXT "
            + ")";
    //这里采用把list 存储为TEXT类型

    public DbOpenHelper(Context context) {
        super(context, "gank", null,1  );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_GANKDAILY);
        db.execSQL(SQL_CREATE_GANKTYPE);
        //外键生效
        //https://www.sqlite.org/foreignkeys.html
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
