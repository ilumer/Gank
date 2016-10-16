package com.fast.ilumer.gank.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;

import java.util.List;

/**
 * Created by root on 10/15/16.
 *
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoHolder> {

    List<GankInfo> mInfos;

    public InfoAdapter(List<GankInfo> infos) {
        super();
        this.mInfos = infos;
    }

    @Override
    public InfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InfoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gankdaily_newsitem,parent,false));
    }

    @Override
    public void onBindViewHolder(InfoHolder holder, int position) {
        GankInfo info = mInfos.get(position);
        holder.BindModel(info,false);
    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }
}
