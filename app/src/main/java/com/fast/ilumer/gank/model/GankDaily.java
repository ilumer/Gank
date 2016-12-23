package com.fast.ilumer.gank.model;

import android.database.Cursor;

import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by root on 10/16/16.
 * gank daily model
 * 变量命名的(O_O)？不想改Android
 */

public class GankDaily {
    public List<GankInfo> Android;
    public List<GankInfo> iOS;
    @SerializedName("前端")
    public List<GankInfo> Fontend;
    @SerializedName("瞎推荐")
    public List<GankInfo> Recommd;
    @SerializedName("休息视频")
    public List<GankInfo> Video;
    @SerializedName("拓展资源")
    public List<GankInfo> Resources;
    @SerializedName("福利")
    public List<GankInfo> Meizi;

    @Override
    public boolean equals(Object obj) {
        boolean results = false;
        if (obj instanceof GankDaily){
            results = this.Meizi.get(0).equals(((GankDaily) obj).Meizi.get(0));
        }
        return results;
    }

    public static final Func1<Cursor,GankDaily> parse = new Func1<Cursor, GankDaily>() {

        @Override
        public GankDaily call(Cursor cursor) {
            GankDaily daily = new GankDaily();
            daily.Android = new ArrayList<>();
            //http://stackoverflow.com/questions/5552258/collections-emptylist-vs-new-instance
            daily.Meizi = new ArrayList<>();
            daily.Video = new ArrayList<>();
            daily.iOS = new ArrayList<>();
            daily.Resources = new ArrayList<>();
            daily.Fontend = new ArrayList<>();
            daily.Recommd = new ArrayList<>();
            try {
                do{
                    GankInfo info = new GankInfo();
                    info.setDatebaseId(Db.getInt(cursor, GankInfoContract.GankEntry._ID));
                    info.set_id(Db.getString(cursor, GankInfoContract.GankEntry.URL_ID));
                    info.setPublishedAt(Util.dateparse(Db.getString(cursor,GankInfoContract.GankEntry.PUBLISHRD_AT)));
                    info.setCreatedAt(Util.dateparse(Db.getString(cursor,GankInfoContract.GankEntry.CREATED_AT)));
                    info.setType(Db.getString(cursor,GankInfoContract.GankEntry.TYPE));
                    info.setWho(Db.getString(cursor, GankInfoContract.GankEntry.WHO));
                    info.setDesc(Db.getString(cursor,GankInfoContract.GankEntry.DESC));
                    info.setUrl(Db.getString(cursor,GankInfoContract.GankEntry.URL));
                    info.setUsed(Db.getBoolean(cursor,GankInfoContract.GankEntry.USED));
                    switch (info.getType()){
                        case "Android":{
                            daily.Android.add(info);
                            break;
                        }
                        case "iOS":{
                            daily.iOS.add(info);
                            break;
                        }
                        case "前端":{
                            daily.Fontend.add(info);
                            break;
                        }
                        case "瞎推荐":{
                            daily.Recommd.add(info);
                            break;
                        }
                        case "拓展资源":{
                            daily.Resources.add(info);
                            break;
                        }
                        case "休息视频":{
                            daily.Video.add(info);
                            break;
                        }
                        case "福利":{
                            daily.Meizi.add(info);
                            break;
                        }
                    }
                }while (cursor.moveToNext());
            }finally {
                cursor.close();
            }
            return daily;
        }
    };
}
