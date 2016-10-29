package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.InfoAdapter;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.DividerItemDecoration;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;
import com.fast.ilumer.gank.rx.HandleErrorTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by root on 10/15/16.
 *
 */

public class GankTypeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TYPE_FLAG = "GankTypeFragment.Type";
    @BindView(R.id.fragment_recyclerview)
    RecyclerView mContentRv;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<GankInfo> mContentList = new ArrayList<>();
    InfoAdapter mContentAdapter;
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    CompositeSubscription mSubscription;
    Unbinder unbinder;
    String type;
    int number = 10;
    int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(TYPE_FLAG);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentAdapter = new InfoAdapter(mContentList,getActivity());
        mContentRv.setAdapter(mContentAdapter);
        mContentRv.setLayoutManager(mLayoutManager);
        mContentRv.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mContentRv.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page) {
               mSubscription.add(getResult(page)
                        .doOnSubscribe(mContentAdapter)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mContentAdapter));
            }
        });
        mContentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        onRefresh();
        mSubscription.add(
                RetrofitHelper.getInstance().getGankDaily().GankTypeInfo(type,number,page)
                .compose(new HandleErrorTransformer())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mContentAdapter));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mSubscription.clear();
    }

    public static GankTypeFragment newInstance(String type){
        Bundle args = new Bundle();
        args.putString(TYPE_FLAG,type);
        GankTypeFragment frgment = new GankTypeFragment();
        frgment.setArguments(args);
        return frgment;
    }

    @Override
    public void onRefresh() {
        if (!mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(true);
        }
        mSwipeRefreshLayout.setRefreshing(true);
        mSubscription.add(getResult(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mContentAdapter));

    }
    public Observable<List<GankInfo>> getResult(int page){
        return RetrofitHelper.getInstance().getGankDaily().GankTypeInfo(type,number,page)
                .compose(new HandleErrorTransformer())
                .subscribeOn(Schedulers.io());
    }
}
