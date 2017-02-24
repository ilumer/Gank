package com.fast.ilumer.gank.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.fast.ilumer.gank.App;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.ProgressAdapter;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;
import com.fast.ilumer.gank.rx.SubscriptionManager;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilumer on 1/5/17.
 * init base recyclerView with swipefragment Fragment
 */

public abstract class RecyclerViewFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener{
    public static final String EXTRA_TYPE = "RecyclerViewFragment.Type";
    public static final String EXTRA_LIST = "RecyclerViewFragment.List";
    public static final String EXTRA_LAYOUTMANAGER_STATE = "RecyclerViewFragment.LayoutManagerState";
    public static final String EXTRA_PAGE = "RecyclerViewFragment.PAGE";
    @BindView(R.id.content)
    RecyclerView mContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<GankInfo> mContentList = new ArrayList<>();
    int currentPage = 1;
    ProgressAdapter mAdapter;

    String type;
    BriteDatabase db;
    DbInstance dbInstance;
    Parcelable layoutManagerState;
    EndlessRecyclerOnScrollListener scrollListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(EXTRA_TYPE);
        subscription = new CompositeSubscription();
        Log.e(type,"onCreate");
        if (savedInstanceState!=null){
            mContentList = savedInstanceState.getParcelableArrayList(EXTRA_LIST);
            layoutManagerState = savedInstanceState.getParcelable(EXTRA_LAYOUTMANAGER_STATE);
            currentPage = savedInstanceState.getInt(EXTRA_PAGE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("onAttach", EXTRA_TYPE);
        try {
            dbInstance = ((DbInstance) context);
        }catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " must implement DbInstance");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.e(type,"onViewCreated");
        db = dbInstance.instance();
        mAdapter = getAdapter(mContentList);
        mAdapter.setHasStableIds(true);
        mContent.setAdapter(mAdapter);
        mContent.setLayoutManager(getLayoutManager());
        addDivider(mContent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        scrollListener = new EndlessRecyclerOnScrollListener(currentPage,mContent.getLayoutManager()) {
            @Override
            public void onLoadMore(int page) {
                loadMore(page);
            }

        };
        mContent.setHasFixedSize(true);
        mContent.addOnScrollListener(scrollListener);
        if (savedInstanceState!=null){
            mContent.getLayoutManager().onRestoreInstanceState(layoutManagerState);
        }else {
            subscription.add(db.createQuery(Db.TYPE_TABLE_NAME, "select * from " + Db.TYPE_TABLE_NAME + " where " + GankInfoContract.GankEntry.TYPE + " = ?", type)
                    .mapToOne(mapToList)
                    .filter(list -> list.size() != 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        if (mContentList.size() >= 10) {
                            for (int i = 0; i < list.size(); i++) {
                                mContentList.set(i, list.get(i));
                            }
                        } else {
                            mContentList.addAll(list);
                        }
                        mAdapter.notifyDataSetChanged();
                    }));
            onRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void loadMore(int page){
        subscription.add(getReslut(page)
                .doOnSubscribe(mAdapter)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter));
    }

    public RecyclerView.Adapter getAdapter(){
        return mContent.getAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        layoutManagerState = mContent.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(EXTRA_LAYOUTMANAGER_STATE,layoutManagerState);
        outState.putParcelableArrayList(EXTRA_LIST,(ArrayList<? extends Parcelable>) mContentList);
        outState.putInt(EXTRA_PAGE,scrollListener.getCurrentPage());
    }

    private final Func1<Cursor, List<GankInfo>> mapToList = new Func1<Cursor, List<GankInfo>>() {
        @Override
        public List<GankInfo> call(Cursor cursor) {
            List<GankInfo> temp = new ArrayList<>();
            cursor.moveToFirst();
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
                    String images = Db.getString(cursor,GankInfoContract.GankEntry.IMAGELIST);
                    info.setImages(images==null?null: Arrays.asList(Util.convertStringToArray(images)));
                    temp.add(info);
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
            return temp;
        }
    };

    @Override
    public void onDetach() {
        dbInstance = null;
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        SubscriptionManager.get().cancel();
    }

    private Observable<List<GankInfo>> getReslut(int page) {
        return RetrofitHelper.getRetrofitInstance().getGank()
                .gankTypeInfo(type, 10, page)
                .map(gankRepositoriesResult -> gankRepositoriesResult.response().body().results)
                .subscribeOn(Schedulers.io());
    }



    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        subscription.add(getReslut(1)
                .filter(list -> !mContentList.containsAll(list))
                .map(list -> {
                    BriteDatabase.Transaction transaction = db.newTransaction();
                    try {
                        db.execute("delete From " + Db.TYPE_TABLE_NAME + " where " + GankInfoContract.GankEntry._ID + " > -1 and " + GankInfoContract.GankEntry.TYPE + " = ?", type);
                        //这里直接使用字符串拼接竟然会直接出现问题
                        //http://stackoverflow.com/questions/21958789/sqlite-insert-issue-error-no-such-column
                        //http://stackoverflow.com/questions/6337296/sqlite-exception-no-such-column-when-trying-to-select}
                        for (GankInfo info:list) {
                            db.insert(Db.TYPE_TABLE_NAME, new GankInfo.Builder(info).build());
                        }
                        transaction.markSuccessful();
                    } finally {
                        transaction.end();
                    }
                    return list;
                })
                .subscribeOn(Schedulers.io())
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

                    }
                }));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recyclerview_fragment_layout;
    }

    protected abstract ProgressAdapter getAdapter(List<GankInfo> mContentList);

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected void addDivider(RecyclerView recyclerView){

    }

    public interface DbInstance {
        BriteDatabase instance();
    }

}
