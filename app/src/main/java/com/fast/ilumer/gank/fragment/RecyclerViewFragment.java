package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.ProgressAdapter;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilumer on 1/5/17.
 * init base recyclerView with swipefragment Fragment
 */

public abstract class RecyclerViewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    public static final String TYPE_FLAG = "RecyclerViewFragment.Type";
    @BindView(R.id.content)
    RecyclerView mContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<GankInfo> mContentList = new ArrayList<>();
    ProgressAdapter mAdapter;
    CompositeSubscription mSubscription;
    String type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(TYPE_FLAG);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = getAdapter(mContentList);
        mContent.setAdapter(mAdapter);
        mContent.setLayoutManager(getLayoutManager());
        mContent.addOnScrollListener(new EndlessRecyclerOnScrollListener(mContent.getLayoutManager()) {
            @Override
            public void onLoadMore(int page) {
                mSubscription.add(getReslut(page)
                        .doOnSubscribe(mAdapter)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mAdapter));
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscription.clear();
    }

    private Observable<List<GankInfo>> getReslut(int page) {
        return RetrofitHelper.getInstance().getGank()
                .gankTypeInfo(type, 10, page)
                .map(gankRepositoriesResult -> gankRepositoriesResult.response().body().results)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mSubscription.add(getReslut(1)
                .filter(list -> mContentList.size() == 0 || mContentList.containsAll(list))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankInfo>>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<GankInfo> list) {
                        if (mContentList.size() > 10) {
                            for (int i = 0; i < list.size(); i++) {
                                mContentList.set(i, list.get(i));
                            }
                            mAdapter.notifyItemRangeChanged(0, list.size());
                        } else {
                            mContentList.addAll(list);
                            mAdapter.notifyItemRangeInserted(0, list.size());
                        }
                    }
                }));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recyclerview_fragment_layout;
    }

    protected abstract ProgressAdapter getAdapter(List<GankInfo> mContentList);

    protected abstract RecyclerView.LayoutManager getLayoutManager();
}
