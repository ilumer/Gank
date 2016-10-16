package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by root on 10/16/16.
 * -
 */

public abstract class BaseFragment extends Fragment {


    @BindView(R.id.fragment_recyclerview)
    RecyclerView mContentRv;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefresh;
    Unbinder unbinder;
    RecyclerView.LayoutManager mLayoutManager = getLayoutManager();
    RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mAdapter = getAdapter();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRefresh.setOnRefreshListener(getRefreshListener());
        mContentRv.setHasFixedSize(true);
        mContentRv.setLayoutManager(mLayoutManager);
        mContentRv.setAdapter(mAdapter);
    }

    protected abstract SwipeRefreshLayout.OnRefreshListener getRefreshListener();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter();
}
