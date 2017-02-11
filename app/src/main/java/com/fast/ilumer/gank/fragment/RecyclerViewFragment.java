package com.fast.ilumer.gank.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.ProgressAdapter;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.recyclerview.EndlessRecyclerOnScrollListener;
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
    BriteDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(TYPE_FLAG);
        mSubscription = new CompositeSubscription();
        Log.e(type,"onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            db = ((InstanceDb) context).instance();
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString() + " must implement InstanceDb");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.e(type,"onViewCreated");
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
        addDivider(mContent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSubscription.add(db.createQuery(Db.TYPE_TABLE_NAME,"select * from "+Db.TYPE_TABLE_NAME+" where "+GankInfoContract.GankEntry.TYPE+" = ?",type)
               .mapToOne(mapToList)
               .filter(list -> list.size()!=0)
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(list -> {
                   if (mContentList.size()>=10){
                       for (int i= 0;i<list.size();i++){
                           mContentList.set(i,list.get(i));
                       }
                   }else {
                       mContentList.addAll(list);
                   }
                   mAdapter.notifyDataSetChanged();
               }));
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        Log.e(type,"onDestoryView");
        super.onDestroyView();
        mSubscription.clear();
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

    public interface InstanceDb{
        BriteDatabase instance();
    }
}
