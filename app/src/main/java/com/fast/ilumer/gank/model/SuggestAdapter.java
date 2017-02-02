package com.fast.ilumer.gank.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.listener.OnClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by ilumer on 1/11/17.
 *
 */

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.SuggestionVH>
        implements Action1<List<SearchRepo>>{

    private List<SearchRepo> mSuggestionList;
    private OnClickListener mlistener;

    public SuggestAdapter(List<SearchRepo> mSuggestionList) {
        super();
        this.mSuggestionList = mSuggestionList;
    }

    @Override
    public SuggestionVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuggestionVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_suggestion_rv_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(SuggestionVH holder, int position) {
        holder.bindModel(mSuggestionList.get(position).getShowItem());
        holder.itemView.setOnClickListener(v -> {
            mlistener.OnClick(mSuggestionList.get(position));
        });
    }

    public void setOnClicklistener(OnClickListener listener){
        mlistener = listener;
    }

    @Override
    public int getItemCount() {
        return mSuggestionList.size();
    }

    static class SuggestionVH extends RecyclerView.ViewHolder{
        @BindView(R.id.search_suggestion)
        TextView mSuggestion;
        @BindView(R.id.list_item)
        View itemView;
        public SuggestionVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        protected void bindModel(String item){
            mSuggestion.setText(item);
        }
    }

    @Override
    public void call(List<SearchRepo> searchRepos) {
        mSuggestionList = searchRepos;
        notifyDataSetChanged();
    }
}
