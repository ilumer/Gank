package com.fast.ilumer.gank.model;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.viewholder.InfoAllHolder;
import com.fast.ilumer.gank.model.viewholder.InfoHolder;
import com.fast.ilumer.gank.model.viewholder.InfoPicHolder;

import java.util.List;

/**
 * Created by root on 10/15/16.
 *
 */

public class InfoAdapter extends ProgressAdapter{
    public static final int GANK_TYPE_INFO =1;
    public static final int GANK_INFO_PIC = 2;
    public static final int GANK_INFO_PIC_INDICATE = 3;
    private Activity mActivity;

    public InfoAdapter(List<GankInfo> mContentList,Activity activity) {
        super(mContentList);
        this.mActivity = activity;
    }

    @Override
    public int getItemViewTypeExt(int position) {
        if (getmContent().get(position).getImages()==null) {
            return GANK_TYPE_INFO;
        }else if (getmContent().get(position).getImages().size()==1){
            return GANK_INFO_PIC;
        }else {
            return GANK_INFO_PIC_INDICATE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateExtViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case GANK_INFO_PIC:{
                return new InfoPicHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gank_info_pic,parent,false));
            }
            case GANK_TYPE_INFO:{
                return new InfoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gank_info,parent,false));
            }
            default:
                return new InfoAllHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gank_info_pic_indicate,parent,false));
        }
    }

    @Override
    public void onBindExtViewHolder(RecyclerView.ViewHolder holder, GankInfo info) {
        ((InfoHolder) holder).bindModel(info);
        ((InfoHolder) holder).bottomBackground.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent intent = builder.build();
            intent.launchUrl(mActivity, Uri.parse(info.getUrl()));
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof InfoHolder) {
            ((InfoHolder) holder).onAttach();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof InfoHolder) {
            ((InfoHolder) holder).onDetach();
        }
    }
}
