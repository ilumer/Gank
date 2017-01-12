package com.fast.ilumer.gank.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.DbOpenHelper;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.ProgressAdapter;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
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
    private SqlBrite sqlBrite = new SqlBrite.Builder().build();
    private BriteDatabase db ;
    private final PublishSubject<Integer> mGankTypeSubject = PublishSubject.create();
    List<GankInfo> mContentList = new ArrayList<>();
    ProgressAdapter mAdapter;
    CompositeSubscription mSubscription;
    String type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(TYPE_FLAG);
        db = sqlBrite.wrapDatabaseHelper(new DbOpenHelper(getActivity()),Schedulers.io());
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
        });mSwipeRefreshLayout.setOnRefreshListener(this);
        mSubscription.add(Observable.fromCallable(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return db.query("select * from"+Db.TYPE_TABLE_NAME);
            }
        })
                .map(mapToList)
                .filter(list -> !list.isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter));
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

    private final Func1<Cursor, List<GankInfo>> mapToList = new Func1<Cursor, List<GankInfo>>() {
        @Override
        public List<GankInfo> call(Cursor cursor) {
            List<GankInfo> temp = new ArrayList<>();
            try {
                do {
                    GankInfo info = new GankInfo();
                    info.setDatebaseId(Db.getInt(cursor, GankInfoContract.GankEntry._ID));
                    info.set_id(Db.getString(cursor, GankInfoContract.GankEntry.URL_ID));
                    info.setPublishedAt(Util.dateparse(Db.getString(cursor, GankInfoContract.GankEntry.PUBLISHRD_AT)));
                    info.setCreatedAt(Util.dateparse(Db.getString(cursor, GankInfoContract.GankEntry.CREATED_AT)));
                    info.setType(Db.getString(cursor, GankInfoContract.GankEntry.TYPE));
                    info.setWho(Db.getString(cursor, GankInfoContract.GankEntry.WHO));
                    info.setDesc(Db.getString(cursor, GankInfoContract.GankEntry.DESC));
                    info.setUrl(Db.getString(cursor, GankInfoContract.GankEntry.URL));
                    info.setUsed(Db.getBoolean(cursor, GankInfoContract.GankEntry.USED));
                    temp.add(info);
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
            return temp;
        }
    };


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
                .filter(list -> mContentList.isEmpty() || mContentList.containsAll(list))
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
                        if (mContentList.size() >= 10) {
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
