package com.fast.ilumer.gank.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ${ilumer} on 1/29/17.
 */

public class GankTypeProvider extends ContentProvider {

    private static final UriMatcher mUriMatcher = buildUriMatcher() ;
    static final int GANKTYPE = 100;
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
                Log.e(projection[0],uri.toString());
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
            default:
                throw new UnsupportedOperationException("unexpected uri :"+uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match  = mUriMatcher.match(uri);
        switch (match){
            case GANKTYPE:
                return GankInfoContract.GankEntry.CONTENT_TYPE;
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
            default:
                throw new UnsupportedOperationException("unexpected uri :"+uri);
        }
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
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
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
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        return rowUpdated;
    }

    static UriMatcher buildUriMatcher(){
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GankInfoContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,Db.TYPE_TABLE_NAME, GANKTYPE);
        return matcher;
    }
}
