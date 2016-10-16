package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.InfoAdapter;
import com.fast.ilumer.gank.network.Gank;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.rx.RxUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by root on 10/15/16.
 *
 */

public class GankTypeFragment extends Fragment {

    public static final String TYPE_FLAG = "GankTypeFragment.Type";
    @BindView(R.id.fragment_recyclerview)
    RecyclerView mContentRv;
    List<GankInfo> mContentList = new ArrayList<>();
    InfoAdapter mContentAdapter;
    LinearLayoutManager mLayoutManager;
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
        mContentRv.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mContentRv.setLayoutManager(mLayoutManager);
        mContentAdapter = new InfoAdapter(mContentList);
        mContentRv.setAdapter(mContentAdapter);
        mSubscription = RetrofitHelper.getInstance()
                .getGankDaily().GankTypeInfo(type,number,page)
                .flatMap(new Func1<Gank.Result<List<GankInfo>>, Observable<List<GankInfo>>>() {
                    @Override
                    public Observable<List<GankInfo>> call(Gank.Result<List<GankInfo>> listResult) {
                        return RxUtil.flatResult(listResult);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GankInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("tag","error");
                    }

                    @Override
                    public void onNext(List<GankInfo> infos) {
                        mContentList.addAll(infos);
                        mContentAdapter.notifyDataSetChanged();
                    }
                });
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


}
