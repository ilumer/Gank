package com.fast.ilumer.gank.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.fast.ilumer.gank.dao.Db.TODAY_TABLE_NAME;

/**
 * Created by root on 11/26/16.
 * create table
 */

public class DbOpenHelper extends SQLiteOpenHelper {



    private static final String SQL_CREATE_GANKDAILY = "create table "+TODAY_TABLE_NAME+" ( "
            +GankInfoContract.GankEntry._ID+" Integer primary key,"
            +GankInfoContract.GankEntry.URL_ID+" TEXT , "
            +GankInfoContract.GankEntry.URL + " TEXT , "
            +GankInfoContract.GankEntry.CREATED_AT +" TEXT ,"
            +GankInfoContract.GankEntry.PUBLISHRD_AT+" TEXT, "
            +GankInfoContract.GankEntry.DESC +" TEXT, "
            +GankInfoContract.GankEntry.WHO +" TEXT, "
            + GankInfoContract.GankEntry.USED+" Interger, "
            + GankInfoContract.GankEntry.TYPE+" TEXT "
            +")";

    public DbOpenHelper(Context context) {
        super(context, "gank", null,1  );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_GANKDAILY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
