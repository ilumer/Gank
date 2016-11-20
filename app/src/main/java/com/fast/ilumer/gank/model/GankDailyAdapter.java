package com.fast.ilumer.gank.model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fast.ilumer.gank.R;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/26/16.
 *
 */

public class GankDailyAdapter extends RecyclerView.Adapter<GankDailyAdapter.DaliyHolder>{

    private static final int TYPE_COMMON_NEWS = 0;
    private static final int TYPE_COMMON_TITLE_NEWS = 1;


    private List<GankInfo> list;
    private Activity host;
    public GankDailyAdapter(List<GankInfo> list, Activity host) {
        this.host =host;
        this.list = list;
    }

    @Override
    public DaliyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gankdaily_item, parent, false);
        return new DaliyHolder(view);
    }

    @Override
    public void onBindViewHolder(DaliyHolder holder, int position) {
        GankInfo info = list.get(position);
        if (position==0){
            holder.bind(info,true);
        }else if (compareType(position-1,position)){
            holder.bind(info,true);
        }else {
            holder.bind(info,false);
        }
    }

    private boolean compareType(int front,int back){
        GankInfo first = list.get(front);
        GankInfo second = list.get(back);
        if (first.getType().equals(second.getType())){
            return false;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DaliyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.type)
        TextView textView;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.referrer)
        TextView referrer;
        @BindString(R.string.none)
        String none;
        @BindString(R.string.referrer)
        String refer;
        public DaliyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        protected void bind(GankInfo info,boolean isType){
            if (isType){
                textView.setText(info.getType());
            }
            content.setText(info.getDesc());
            if (info.getWho()==null){
                textView.setText(none);
            }else {
                textView.setText(String.format(refer,info.getWho()));
            }
        }
    }
}
