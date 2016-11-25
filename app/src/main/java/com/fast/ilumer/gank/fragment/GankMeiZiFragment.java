package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.GankRepositories;
import com.fast.ilumer.gank.model.ImageAdapter;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by root on 10/22/16.
 *
 */

public class GankMeiZiFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener{

    private Unbinder unbinder;
    @BindView(R.id.content)
    RecyclerView content;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<GankInfo> mContentList = new ArrayList<>();
    int number =10;
    ImageAdapter adapter;
    GridLayoutManager manager ;
    CompositeSubscription subscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscription = new CompositeSubscription();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gril_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ImageAdapter(mContentList,this);
        manager = new GridLayoutManager(getActivity(),2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)){
                    case ImageAdapter.VIEW_TYPE_LOADING:{
                        return 2;
                    }
                    case ImageAdapter.VIEW_TYPE_INFO:{
                        return 1;
                    }
                    default:
                        return -1;
                }
            }
        });
        content.setAdapter(adapter);
        content.setLayoutManager(manager);
        content.addOnScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int page) {
                getReslut(page)
                        .doOnSubscribe(adapter)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adapter);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (!mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(true);
        }
        mSwipeRefreshLayout.setRefreshing(true);
        subscription.add(getReslut(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankInfo>>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<GankInfo> infos) {
                        if (mContentList.size()>10) {
                            for (int i = 0; i < infos.size(); i++) {
                                mContentList.set(i, infos.get(i));
                            }
                            adapter.notifyItemRangeChanged(0, infos.size());
                        }else {
                            mContentList.addAll(infos);
                            adapter.notifyItemRangeInserted(0,infos.size());
                        }
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        subscription.clear();
    }

     private Observable<List<GankInfo>> getReslut(int page){
        return RetrofitHelper.getInstance().getGank()
                .GankTypeInfo("福利",number,page)
                .map(new Func1<Result<GankRepositories<List<GankInfo>>>, List<GankInfo>>() {
                    @Override
                    public List<GankInfo> call(Result<GankRepositories<List<GankInfo>>> gankRepositoriesResult) {
                        return gankRepositoriesResult.response().body().results;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
