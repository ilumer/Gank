package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;
import com.fast.ilumer.gank.rx.HandleErrorTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by root on 10/15/16.
 *
 */

public class GankTypeFragment extends BaseFragment {

    public static final String TYPE_FLAG = "GankTypeFragment.Type";
    //Subscriber实现了Subscription不能够实现复用
    private Observer<List<GankInfo>> mDataObserver = new Observer<List<GankInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<GankInfo> infos) {
            mContentList.addAll(infos);
            mContentAdapter.notifyDataSetChanged();
        }
    };
    RecyclerView mContentRv;
    List<GankInfo> mContentList = new ArrayList<>();
    InfoAdapter mContentAdapter;
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    boolean mIsLoading = false;
    Subscription mSubscription;
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentRv = getmContentRv();
        mContentRv.addOnScrollListener(new EndlessRecyclerOnScrollListener(getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                if (!mIsLoading) {
                    mSubscription = RetrofitHelper.getInstance()
                            .getGankDaily().GankTypeInfo(type, number, current_page)
                            .compose(new HandleErrorTransformer())
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    mContentAdapter.startLoadingMore();
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<GankInfo>>() {
                                @Override
                                public void call(List<GankInfo> infos) {
                                    mContentAdapter.endLoadingMore();
                                    mContentList.addAll(infos);
                                    mContentAdapter.notifyDataSetChanged();
                                    mIsLoading = false;
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    mContentAdapter.endLoadingMore();
                                    mIsLoading = false;
                                }
                            });
                }
            }
        });
        mSubscription = RetrofitHelper.getInstance()
                .getGankDaily().GankTypeInfo(type,number,page)
                .compose(new HandleErrorTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDataObserver);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    public static GankTypeFragment newInstance(String type){
        Bundle args = new Bundle();
        args.putString(TYPE_FLAG,type);
        GankTypeFragment frgment = new GankTypeFragment();
        frgment.setArguments(args);
        return frgment;
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener getRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mIsLoading){
                    mSubscription = RetrofitHelper.getInstance()
                            .getGankDaily().GankTypeInfo(type,number,page)
                            .compose(new HandleErrorTransformer())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<GankInfo>>() {
                                @Override
                                public void call(List<GankInfo> infos) {
                                    mContentList.clear();
                                    mContentList.addAll(infos);
                                    //是否可以直接替换刷新的数据
                                    mRefresh.setRefreshing(false);
                                }
                            });
                }
            }


        };
    }

    @Override
    protected LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        mContentAdapter = new InfoAdapter(mContentList,getActivity());
        return mContentAdapter;
    }
}
