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
import com.fast.ilumer.gank.model.GankRepositories;
import com.fast.ilumer.gank.model.InfoAdapter;
import com.fast.ilumer.gank.network.Results;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.DividerItemDecoration;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;
import com.squareup.sqlbrite.BriteDatabase;

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
 * Created by root on 10/15/16.
 *
 */

public class GankTypeFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TYPE_FLAG = "GankTypeFragment.Type";
    private BriteDatabase db;

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
        mSwipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
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
                .filter(new Func1<List<GankInfo>, Boolean>() {
                    @Override
                    public Boolean call(List<GankInfo> list) {
                        return mContentList.size()==0||mContentList.containsAll(list);
                    }
                })
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
                if (mContentList.size()>10) {
                    for (int i = 0; i < list.size(); i++) {
                        mContentList.set(i, list.get(i));
                    }
                    mContentAdapter.notifyItemRangeChanged(0, list.size());
                }else {
                    mContentList.addAll(list);
                    mContentAdapter.notifyItemRangeInserted(0,list.size());
                }
            }
        }));

    }
    public Observable<List<GankInfo>> getResult(int page){
        return RetrofitHelper.getInstance().getGank().GankTypeInfo(type,number,page)
                .filter(Results.isSuccessful())
                .map(new Func1<Result<GankRepositories<List<GankInfo>>>, List<GankInfo>>() {
                    @Override
                    public List<GankInfo> call(Result<GankRepositories<List<GankInfo>>> gankRepositoriesResult) {
                        return gankRepositoriesResult.response().body().results;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
