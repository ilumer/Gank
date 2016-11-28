package com.fast.ilumer.gank.model;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;

import java.util.List;

/**
 * Created by root on 10/15/16.
 *
 */

public class InfoAdapter extends ProgressAdapter{

    private Activity mActivity;

    public InfoAdapter(List<GankInfo> mContentList,Activity activity) {
        super(mContentList);
        this.mActivity = activity;
    }

    @Override
    RecyclerView.ViewHolder getInfoViewHolder(ViewGroup parent) {
        return new InfoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ganktypeinfo_item, parent, false));
    }

    @Override
    void BindInfoViewHolder(GankInfo info, RecyclerView.ViewHolder holder) {
        ((InfoHolder) holder).BindModel(info);
        final String uri = info.getUrl();
        holder.itemView.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent intent = builder.build();
            intent.launchUrl(mActivity, Uri.parse(uri));
        });
    }
}
