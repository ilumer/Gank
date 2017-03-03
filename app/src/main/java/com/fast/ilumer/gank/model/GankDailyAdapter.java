package com.fast.ilumer.gank.model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.activity.PictureActivity;
import com.fast.ilumer.gank.activity.WebViewActivity;
import com.fast.ilumer.gank.model.viewholder.GirlHolder;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/26/16.
 *
 */

public class GankDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_COMMON_NEWS = 0;
    private static final int TYPE_COMMON_TITLE_NEWS = 1;
    private static final int TYPE_HEADER_VIEW= 2;


    private List<GankInfo> list;
    private Activity host;
    public GankDailyAdapter(List<GankInfo> list, Activity host) {
        this.host =host;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType!=TYPE_HEADER_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gank_daily_info, parent, false);
            return new DaliyHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gank_girl_pic,parent,false);
            return new GirlHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GankInfo info = list.get(position);
        switch (getItemViewType(position)){
            case TYPE_COMMON_TITLE_NEWS: {
                ((DaliyHolder)holder).bind(info, true,host);
                break;
            }
            case TYPE_COMMON_NEWS:{
                ((DaliyHolder)holder).bind(info,false,host);
                break;
            }
            case TYPE_HEADER_VIEW:{
                ((GirlHolder)holder).setAspectRatio(1.2f);
                ((GirlHolder)holder).bindModel(info);
                ((GirlHolder)holder).imageView.setOnClickListener(v -> {
                    Intent i = new Intent(host,PictureActivity.class);
                    i.putExtra(PictureActivity.INTENT_EXTRA_URL,info.getUrl());
                    host.startActivity(i);
                });
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
            return TYPE_HEADER_VIEW;
        } else if (compareType(position-1,position)){
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

        protected void bind(GankInfo info,boolean isType,Activity host){
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
            builder.setSpan(new TextAppearanceSpan(host,R.style.desc_TextView_style),0,info.getDesc().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            builder.setSpan(new TextAppearanceSpan(host,R.style.refer_TextView_style),info.getDesc().length()+1,builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //http://stackoverflow.com/questions/8999781/android-two-sentences-two-styles-one-textview
            //实现两个style的textview
            content.setText(builder);
            content.setOnClickListener(v -> {
                if (!Util.getPreferredLoadWebView(host)) {
                    Util.getCustomIntent(host)
                            .build()
                            .launchUrl(host, Uri.parse(info.getUrl()));
                }else {
                    Intent i = new Intent(host, WebViewActivity.class);
                    i.putExtra(WebViewActivity.EXTRA_INTENT_URL,info.getUrl());
                    host.startActivity(i);
                }
            });
        }
    }
}
