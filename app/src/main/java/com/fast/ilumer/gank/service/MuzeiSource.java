package com.fast.ilumer.gank.service;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.fast.ilumer.gank.activity.MainActivity;
import com.fast.ilumer.gank.activity.PictureActivity;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.MuzeiArtSource;

/**
 * Created by ilumer on 17-2-24.
 */

public class MuzeiSource extends MuzeiArtSource{

    public MuzeiSource(){
        this("MuzeiSource");
    }

    public MuzeiSource(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        if (isEnabled()){
            onUpdate(0);
        }
    }

    @Override
    protected void onUpdate(int reason) {
        //从cursor中找出最新更新的资料
        String url = null;
        Cursor cursor = getContentResolver().query(
                GankInfoContract.GankEntry.CONTENT_URI,
                new String[]{GankInfoContract.GankEntry.URL},
                GankInfoContract.GankEntry.TYPE+" = ?",
                new String[]{"福利"},
                GankInfoContract.GankEntry.PUBLISHRD_AT + " DESC"
        );
        if (cursor!=null) {
            cursor.moveToFirst();
            url = Db.getString(cursor,GankInfoContract.GankEntry.URL);
            cursor.close();
        }
        if (url!=null) {
            publishArtwork(new Artwork.Builder()
                    .imageUri(Uri.parse(url))
                    .build());
        }
    }
}
