package com.fast.ilumer.gank.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fast.ilumer.gank.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.functions.Action0;

/**
 * Created by root on 10/22/16.
 *为recyclerview添加一个Progressbar
 */

public abstract class ProgressAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements Action0, Observer<List<GankInfo>>{

    public static final int GANK_VIEW_LOADING = 0;
    private boolean isLoadingMore = false;

    private List<GankInfo> mContentList;

    public ProgressAdapter(List<GankInfo> mContentList) {
        this.mContentList = mContentList;
    }

    private void startLoadingMore() {
        if (isLoadingMore) return;
        isLoadingMore = true;
        notifyItemInserted(getDataCount());
    }


    private void endLoadingMore() {
        if (!isLoadingMore) return;
        notifyItemRemoved(getDataCount());
        isLoadingMore = false;
    }

    @Override
    public void call()  {
        startLoadingMore();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        //处理可能遇到的问题
    }

    @Override
    public void onNext(List<GankInfo> infos) {
        int position = getItemCount();
        mContentList.addAll(infos);
        this.notifyItemRangeInserted(position,infos.size());
        endLoadingMore();
    }

    @Override
    public int getItemCount() {
        return getDataCount()+(isLoadingMore?1:0);
    }

    public int getDataCount(){
        return mContentList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType== GANK_VIEW_LOADING){
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gank_progressbar,parent,false));
        }else {
            return onCreateExtViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case GANK_VIEW_LOADING:{
                bindProgress((ProgressViewHolder) holder);
                break;
            }
            default: {
                onBindExtViewHolder(holder,mContentList.get(position));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getDataCount()>0&&position< getDataCount()){
            return getItemViewTypeExt(position);
        }
        return GANK_VIEW_LOADING;
    }

    private void bindProgress(ProgressViewHolder viewHolder){
        viewHolder.progressBar.setVisibility(isLoadingMore?View.VISIBLE:View.INVISIBLE);
    }

    protected List<GankInfo> getmContent(){
        return mContentList;
    }

    protected void setmContent(List<GankInfo> mContentList){
        this.mContentList =mContentList;
    }

    public abstract int getItemViewTypeExt(int position);
    public abstract RecyclerView.ViewHolder onCreateExtViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindExtViewHolder(RecyclerView.ViewHolder holder, GankInfo info);

    static class ProgressViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.loading)
        ProgressBar progressBar;
        private ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
