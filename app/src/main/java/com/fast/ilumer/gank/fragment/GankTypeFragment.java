package com.fast.ilumer.gank.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.InfoAdapter;
import com.fast.ilumer.gank.model.ProgressAdapter;

import java.util.List;

/**
 * Created by root on 10/15/16.
 *
 */

public class GankTypeFragment extends RecyclerViewFragment{

    @Override
    protected ProgressAdapter getAdapter(List<GankInfo> mContentList) {
        return new InfoAdapter(mContentList,getActivity());
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }
}
