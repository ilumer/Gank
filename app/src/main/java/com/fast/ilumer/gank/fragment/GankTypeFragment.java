package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.InfoAdapter;
import com.fast.ilumer.gank.model.ProgressAdapter;
import com.fast.ilumer.gank.recyclerview.DividerItemDecoration;

import java.util.List;

/**
 * Created by ilumer on 10/15/16.
 */

public class GankTypeFragment extends RecyclerViewFragment {

  public static GankTypeFragment newInstance(String type) {
    Bundle args = new Bundle();
    args.putString(EXTRA_TYPE, type);
    GankTypeFragment frgment = new GankTypeFragment();
    frgment.setArguments(args);
    return frgment;
  }

  @Override protected ProgressAdapter getAdapter(List<GankInfo> mContentList) {
    InfoAdapter adapter = new InfoAdapter(mContentList, getActivity());
    if (Util.getPreferredLoadInfo(getActivity())) {
      adapter.loadImg(false);
    }
    return adapter;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onStart() {
    super.onStart();
    InfoAdapter adapter = (InfoAdapter) getAdapter();
    if (Util.getPreferredLoadInfo(getActivity())) {
      adapter.loadImg(false);
      adapter.notifyDataSetChanged();
    } else {
      adapter.loadImg(true);
      adapter.notifyDataSetChanged();
    }
  }

  protected RecyclerView.LayoutManager getLayoutManager() {
    return new LinearLayoutManager(getActivity());
  }

  @Override protected void addDivider(RecyclerView recyclerView) {
    recyclerView.addItemDecoration(
        new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
  }
}
