package com.fast.ilumer.gank.model;

/**
 * Created by root on 10/27/16.
 *
 */

public class GankType {
    private String type;
    private int number;
    private int page;

    public GankType(String type, int number, int page) {
        this.type = type;
        this.number = number;
        this.page = page;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
