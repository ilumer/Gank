package com.fast.ilumer.gank.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.ilumer.gank.MainActivity;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.InfoAdapter;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.DividerItemDecoration;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;
import com.fast.ilumer.gank.rx.HandleErrorTransformer;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            db = ((MainActivity) context).getBrite();
        }
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
                        if (mContentList.size()==0){
                            mContentList.addAll(infos);
                            mContentAdapter.notifyItemRangeChanged(0,infos.size());
                        }else {
                            //判断这个时候的数据
                            GankInfo info = mContentList.get(0);
                            int index = infos.indexOf(info);
                            if (index==0){
                                return;
                            }else if (index<number&&index>0){
                                mContentList.addAll(infos.subList(0,index));
                                mContentAdapter.notifyItemRangeInserted(0,index+1);
                            }else if (index==-1){
                                //暂时没有实现缓存所以暂时不会出现这个问题
                            }
                        }
                    }
                }));

    }
    public Observable<List<GankInfo>> getResult(int page){
        return RetrofitHelper.getInstance().getGankDaily().GankTypeInfo(type,number,page)
                .compose(new HandleErrorTransformer())
                .subscribeOn(Schedulers.io());
    }
}
