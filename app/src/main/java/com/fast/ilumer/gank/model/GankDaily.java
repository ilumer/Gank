package com.fast.ilumer.gank.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 10/16/16.
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

}
