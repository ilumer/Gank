package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by root on 10/22/16.
 *
 */

public class GankMeiZiFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.content)
    RecyclerView content;
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
        content.setHasFixedSize(true);
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
        content.setLayoutManager(manager);
        content.setAdapter(adapter);
        subscription.add(getReslut(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter));
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
