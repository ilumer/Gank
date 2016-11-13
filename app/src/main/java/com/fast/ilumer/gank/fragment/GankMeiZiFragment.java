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
import com.fast.ilumer.gank.model.GankType;
import com.fast.ilumer.gank.model.ImageAdapter;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;
import com.fast.ilumer.gank.rx.HandleErrorTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
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
    PublishSubject<GankType> subject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscription = new CompositeSubscription();
        subject = PublishSubject.create();
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
                        if (mContentList.size()==0){
                            mContentList.addAll(infos);
                            adapter.notifyItemRangeChanged(0,infos.size());
                        }else {
                            //判断这个时候的数据
                            GankInfo info = mContentList.get(0);
                            int index = infos.indexOf(info);
                            if (index==0){
                                return;
                            }else if (index<number&&index>0){
                                mContentList.addAll(infos.subList(0,index));
                                adapter.notifyItemRangeInserted(0,index+1);
                            }else if (index==-1){
                                //暂时没有实现缓存所以暂时不会出现这个问题
                            }
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
        return RetrofitHelper.getInstance().getGankDaily()
                .GankTypeInfo("福利",number,page)
                .compose(new HandleErrorTransformer())
                .subscribeOn(Schedulers.io());
    }
}
