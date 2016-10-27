package com.fast.ilumer.gank.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by root on 10/17/16.
 *
 */

public class ProgressViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public ProgressViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView;
    }
}
