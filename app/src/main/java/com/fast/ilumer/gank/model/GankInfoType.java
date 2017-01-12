package com.fast.ilumer.gank.model;

/**
 * Created by root on 1/12/17.
 */

public class GankInfoType {
    private String type;
    private String page;
    private String number;

    public GankInfoType(String type, String page, String number) {
        this.type = type;
        this.page = page;
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
