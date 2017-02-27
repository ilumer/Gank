package com.fast.ilumer.gank.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ${ilumer} on 1/29/17.
 */

public class GankProvider extends ContentProvider {

    private static final UriMatcher mUriMatcher = buildUriMatcher() ;
    static final int GANKTYPE = 100;
    static final int GANKDAILY = 101;
    private DbOpenHelper mDbOpenHelper;

    @Override
    public boolean onCreate() {
        mDbOpenHelper =  new DbOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = mUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case GANKTYPE:{
                retCursor = mDbOpenHelper.getReadableDatabase().query(
                        Db.TYPE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case GANKDAILY:{
                retCursor = mDbOpenHelper.getReadableDatabase().query(
                        Db.TODAY_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("unexpected uri :"+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        retCursor.moveToPosition(-1);
        //http://stackoverflow.com/questions/10723770/whats-the-best-way-to-iterate-an-android-cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match  = mUriMatcher.match(uri);
        switch (match){
            case GANKTYPE:
                return GankInfoContract.GankEntry.TYPE_CONTENT_TYPE;
            case GANKDAILY:
                return GankInfoContract.GankEntry.DAILY_CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("unexpected uri :"+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = mUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case GANKTYPE: {
                long id = mDbOpenHelper.getWritableDatabase().insert(Db.TYPE_TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = GankInfoContract.GankEntry.buildGankTypeUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            case GANKDAILY: {
                long id = mDbOpenHelper.getWritableDatabase().insert(Db.TODAY_TABLE_NAME,null, values);
                if (id > 0) {
                    returnUri = GankInfoContract.GankEntry.buildGankDailyUri(id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("unexpected uri :"+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match  = mUriMatcher.match(uri);
        int rowsDeleted ;
        switch (match){
            case GANKTYPE:{
                rowsDeleted = mDbOpenHelper.getWritableDatabase()
                        .delete(Db.TYPE_TABLE_NAME,selection,selectionArgs);
                break;
            }

            case GANKDAILY:{
                rowsDeleted = mDbOpenHelper.getWritableDatabase()
                        .delete(Db.TODAY_TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        int rowUpdated ;
        switch (match){
            case GANKTYPE:{
                rowUpdated = mDbOpenHelper.getWritableDatabase()
                        .update(Db.TYPE_TABLE_NAME,values,selection,selectionArgs);
                break;
            }

            case GANKDAILY:{
                rowUpdated = mDbOpenHelper.getWritableDatabase()
                        .update(Db.TYPE_TABLE_NAME,values,selection,selectionArgs);
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowUpdated;
    }

    static UriMatcher buildUriMatcher(){
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GankInfoContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,Db.TYPE_TABLE_NAME, GANKTYPE);
        matcher.addURI(authority,Db.TODAY_TABLE_NAME,GANKDAILY);
        return matcher;
    }
}
