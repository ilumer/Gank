package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.GirlAdapter;
import com.fast.ilumer.gank.model.ProgressAdapter;

import java.util.List;

/**
 * Created by root on 10/22/16.
 *
 */

public class GankMeiZiFragment extends RecyclerViewFragment{
    private GirlAdapter girlAdapter;

    public static GankMeiZiFragment newInstance(String type){
        Bundle args = new Bundle();
        args.putString(TYPE_FLAG,type);
        GankMeiZiFragment frgment = new GankMeiZiFragment();
        frgment.setArguments(args);
        return frgment;
    }

    @Override
    protected ProgressAdapter getAdapter(List<GankInfo> mContentList) {
        girlAdapter = new GirlAdapter(mContentList,getActivity());
        return girlAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (girlAdapter.getItemViewType(position)){
                    case GirlAdapter.GANK_VIEW_LOADING:{
                        return 2;
                    }
                    case GirlAdapter.GANK_VIEW_GIRL:{
                        return 1;
                    }
                    default:
                        return -1;
                }
            }
        });
        return layoutManager;
    }
}
