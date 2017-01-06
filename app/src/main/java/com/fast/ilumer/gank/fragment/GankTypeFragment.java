package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
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

    public static GankTypeFragment newInstance(String type){
        Bundle args = new Bundle();
        args.putString(TYPE_FLAG,type);
        GankTypeFragment frgment = new GankTypeFragment();
        frgment.setArguments(args);
        return frgment;
    }

    @Override
    protected ProgressAdapter getAdapter(List<GankInfo> mContentList) {
        return new InfoAdapter(mContentList,getActivity());
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }
}
