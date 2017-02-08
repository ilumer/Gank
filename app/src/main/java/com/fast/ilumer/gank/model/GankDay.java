package com.fast.ilumer.gank.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ilumer on 2/8/17.
 */

public class GankDay implements Parcelable{
    private int year;
    private int month;
    private int day;

    public GankDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public GankDay(int[] time){
        this.year = time[0];
        this.month = time[1];
        this.day = time[2];
    }

    public GankDay() {
    }

    public int getYear() {
        return year;
    }

    public GankDay setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public GankDay setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getMonthForGank(){
        return month+1;
    }

    public int getDay() {
        return day;
    }

    public GankDay setDay(int day) {
        this.day = day;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
    }

    protected GankDay(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
    }

    public static final Creator<GankDay> CREATOR = new Creator<GankDay>() {
        @Override
        public GankDay createFromParcel(Parcel source) {
            return new GankDay(source);
        }

        @Override
        public GankDay[] newArray(int size) {
            return new GankDay[size];
        }
    };
}
