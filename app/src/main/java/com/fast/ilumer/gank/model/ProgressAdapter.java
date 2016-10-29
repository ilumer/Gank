package com.fast.ilumer.gank.model;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_INFO =1;
    private boolean isLoadingMore = false;

    private List<GankInfo> mContentList;

    public ProgressAdapter(List<GankInfo> mContentList) {
        this.mContentList = mContentList;
    }

    private void startLoadingMore() {
        if (isLoadingMore) return;
        isLoadingMore = true;
        notifyItemInserted(getDataCount());
        Log.e("start loading","start loading");
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
        //下拉刷新在这种情况下只有两种可能
        //到到的消息中全部已经获取了
        //部分获取
        //全部获取 分页的问题产生的问题
        mContentList.addAll(infos);
        this.notifyDataSetChanged();
        endLoadingMore();
    }

    @Override
    public int getItemCount() {
        return getDataCount()+(isLoadingMore?1:0);
    }

    private int getDataCount(){
        return mContentList.size();
    }

    abstract RecyclerView.ViewHolder getInfoViewHolder(ViewGroup parent);

    abstract void BindInfoViewHolder(GankInfo info, RecyclerView.ViewHolder holder);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==VIEW_TYPE_LOADING){
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preogress_viewholder,parent,false));
        }else {
            return getInfoViewHolder(parent);
        }
        //这里只有两个itemViewHolder
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case VIEW_TYPE_LOADING:{
                bindProgress((ProgressViewHolder) holder);
                break;
            }
            case VIEW_TYPE_INFO:{
                GankInfo info = mContentList.get(position);
                BindInfoViewHolder(info,holder);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getDataCount()>0&&position< getDataCount()){
            return VIEW_TYPE_INFO;
        }
        return VIEW_TYPE_LOADING;
    }

    private void bindProgress(ProgressViewHolder viewHolder){
        viewHolder.progressBar.setVisibility((isLoadingMore?View.VISIBLE:View.INVISIBLE));
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.loading)
        ProgressBar progressBar;
        private ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
