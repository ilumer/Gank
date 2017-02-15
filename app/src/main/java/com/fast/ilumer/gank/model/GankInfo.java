package com.fast.ilumer.gank.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.dao.GankInfoContract;

import java.util.Date;
import java.util.List;

/**
 * Created by root on 10/15/16.
 */

public class GankInfo implements Parcelable{

    private int datebaseId = -1;
    //对应数据库中的唯一字段
    private String _id;
    private String url;
    private Date publishedAt;
    private Date createdAt;
    private String who;
    private String desc;
    private String type;
    private boolean used;
    private List<String> images;

    public static class Builder{
        ContentValues values = new ContentValues();

        public Builder(GankInfo info) {
            values.put(GankInfoContract.GankEntry.URL_ID,info.get_id());
            values.put(GankInfoContract.GankEntry.URL,info.getUrl());
            values.put(GankInfoContract.GankEntry.CREATED_AT, Util.formatDate(info.getCreatedAt()));
            values.put(GankInfoContract.GankEntry.PUBLISHRD_AT,Util.formatDate(info.getPublishedAt()));
            values.put(GankInfoContract.GankEntry.DESC,info.getDesc());
            values.put(GankInfoContract.GankEntry.TYPE,info.getType());
            values.put(GankInfoContract.GankEntry.WHO,info.getWho());
            values.put(GankInfoContract.GankEntry.USED,info.isUsed());
            if (info.getImages()!=null) {
                values.put(GankInfoContract.GankEntry.IMAGELIST,
                        Util.convertArrayToString(info.getImages().toArray(new String[0])));
                //http://stackoverflow.com/questions/4042434/converting-arrayliststring-to-string-in-java
            }
            if (info.getDatebaseId()!=-1) {
                values.put(GankInfoContract.GankEntry._ID, info.getDatebaseId());
            }
        }

        public ContentValues build(){
            return values;
        }
    }

    public int getDatebaseId() {
        return datebaseId;
    }

    public void setDatebaseId(int datebaseId) {
        this.datebaseId = datebaseId;
        return;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof GankInfo){
            GankInfo temp = (GankInfo)obj;
            result = (this.get_id().equals(temp.get_id()));
        }
        return result;
    }

    //没有重载hashcode;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.datebaseId);
        dest.writeString(this._id);
        dest.writeString(this.url);
        dest.writeLong(this.publishedAt != null ? this.publishedAt.getTime() : -1);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeString(this.who);
        dest.writeString(this.desc);
        dest.writeString(this.type);
        dest.writeByte(this.used ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.images);
    }

    public GankInfo() {
    }

    protected GankInfo(Parcel in) {
        this.datebaseId = in.readInt();
        this._id = in.readString();
        this.url = in.readString();
        long tmpPublishedAt = in.readLong();
        this.publishedAt = tmpPublishedAt == -1 ? null : new Date(tmpPublishedAt);
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.who = in.readString();
        this.desc = in.readString();
        this.type = in.readString();
        this.used = in.readByte() != 0;
        this.images = in.createStringArrayList();
    }

    public static final Creator<GankInfo> CREATOR = new Creator<GankInfo>() {
        @Override
        public GankInfo createFromParcel(Parcel source) {
            return new GankInfo(source);
        }

        @Override
        public GankInfo[] newArray(int size) {
            return new GankInfo[size];
        }
    };
}
