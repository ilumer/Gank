package com.fast.ilumer.gank.model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
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
        switch (getItemViewType(position)){
            case TYPE_COMMON_TITLE_NEWS: {
                holder.bind(info, true);
                break;
            }
            case TYPE_COMMON_NEWS:{
                holder.bind(info,false);
                break;
            }
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

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_COMMON_TITLE_NEWS;
        }else if (compareType(position-1,position)){
            return TYPE_COMMON_TITLE_NEWS;
        }
        else {
            return TYPE_COMMON_NEWS;
        }
    }

    public static class DaliyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.content)
        TextView content;
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
                type.setText(info.getType());
                type.setVisibility(View.VISIBLE);
            }
            SpannableStringBuilder builder = new SpannableStringBuilder(info.getDesc());
            //用一个textview表示全部的内容就可以避免了两个textview第二个会出现换行的问题
            if (info.getWho()==null){
                builder.append(String.format(refer,none));
            }else {
                builder.append(String.format(refer,info.getWho()));
            }
            builder.setSpan(new StyleSpan(R.style.desc_TextView_style),0,info.getDesc().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(R.style.refer_TextView_style),info.getDesc().length()+1,builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            content.setText(builder);
        }
    }
}
