package com.fast.ilumer.gank.model;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;

import java.util.List;

/**
 * Created by root on 9/26/16.
 *
 */

public class GankDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_COMMON_NEWS = 0;
    private static final int TYPE_COMMON_TITLE_NEWS = 1;
    private static final int TYPE_IMAGE =2;

    private String typeHelper;
    private List<GankInfo> itemList;
    private Fragment host;
    public GankDailyAdapter(List<GankInfo> itemList, Fragment fragment) {
        super();
        this.host =fragment;
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_COMMON_NEWS:
            case TYPE_COMMON_TITLE_NEWS:{
                return new InfoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gankdaily_newsitem,parent,false));
            }
            case TYPE_IMAGE:{
                return new PhotosViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gankdaily_photoitem,parent,false));
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GankInfo item = itemList.get(position);
        switch (getItemViewType(position)){
            case TYPE_COMMON_NEWS:{
                if (holder instanceof InfoHolder){
                    ((InfoHolder) holder).BindModel(item,false);
                }
                break;
            }
            case TYPE_COMMON_TITLE_NEWS:{
                if (holder instanceof InfoHolder){
                    ((InfoHolder) holder).BindModel(item,true);
                }
                break;
            }
            case TYPE_IMAGE:{
                if (holder instanceof PhotosViewHolder){
                    ((PhotosViewHolder) holder).BindModel(item,true,host);
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0&&itemList.size()!=0){
            return TYPE_COMMON_TITLE_NEWS;
        }else if ("福利".equals(itemList.get(position).getType())){
            return TYPE_IMAGE;
        }else if (!itemList.get(position-1).getType()
                .equals(itemList.get(position).getType())){
            return TYPE_COMMON_TITLE_NEWS;
        }else {
            return TYPE_COMMON_NEWS;
        }
    }
}
