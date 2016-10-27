package com.fast.ilumer.gank.model;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;

import java.util.List;

/**
 * Created by root on 10/15/16.
 *
 */

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_INFO =1;

    private boolean mLoadingMore = false;
    private Activity mActivity;

    List<GankInfo> mInfos;


    public InfoAdapter(List<GankInfo> infos, Activity activity) {
        super();
        this.mInfos = infos;
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        RecyclerView.ViewHolder vHolder;
        if (viewType == VIEW_TYPE_INFO) {
            vHolder = new InfoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gankdaily_newsitem, parent, false));
        }else {
            vHolder = new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preogress_viewholder,parent,false));
        }
        return vHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InfoHolder) {
            GankInfo info = mInfos.get(position);
            ((InfoHolder) holder).BindModel(info,false);
            final String uri = info.getUrl();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(mActivity.getResources().getColor(R.color.colorPrimaryDark));
                    CustomTabsIntent intent = builder.build();
                    intent.launchUrl(mActivity, Uri.parse(uri));
                    //customTabs的简单使用
                }
            });
        }else if (holder instanceof ProgressViewHolder){
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return getDataItemCount()+(mLoadingMore?1:0);
    }

    public int getDataItemCount(){
        return mInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getDataItemCount()>0&&position<getDataItemCount()){
            return VIEW_TYPE_INFO;
        }
        return VIEW_TYPE_LOADING;
    }

    public void startLoadingMore() {
        if (mLoadingMore) return;
        mLoadingMore = true;
        notifyItemInserted(getDataItemCount());
    }


    public void endLoadingMore() {
        if (!mLoadingMore) return;
        notifyItemRemoved(getDataItemCount());
        mLoadingMore = false;
    }
}
