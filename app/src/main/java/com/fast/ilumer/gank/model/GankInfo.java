package com.fast.ilumer.gank.model;

import java.util.Date;

/**
 * Created by root on 10/15/16.
 */

public class GankInfo {

    private String _id;
    private String url;
    private Date publishedAt;
    private Date createdAt;
    private String who;
    private String desc;
    private String type;
    private boolean used;

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
}
