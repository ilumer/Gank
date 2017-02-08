package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.DbOpenHelper;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.fast.ilumer.gank.fragment.DatePickerDialogFragment;
import com.fast.ilumer.gank.fragment.TipDialogFragment;
import com.fast.ilumer.gank.model.GankDaily;
import com.fast.ilumer.gank.model.GankDailyAdapter;
import com.fast.ilumer.gank.model.GankDay;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.GankRepositories;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.rx.Funcs;
import com.fast.ilumer.gank.rx.Results;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class GankTodayActivity extends AppCompatActivity
        implements DatePickerDialogFragment.onDataSetListener{

    private CompositeSubscription subscription;
    private GankDailyAdapter adapter;
    private LinearLayoutManager linearlayoutManager;
    private List<GankInfo> contentList = new ArrayList<>();
    private SqlBrite sqlBrite = new SqlBrite.Builder().build();
    private BriteDatabase db ;
    private GankDaily temp;
    private GankDay day;
    private PublishSubject<GankDay> subject;
    @BindView(R.id.content)
    RecyclerView content;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.empty)
    TextView textView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_gank);
        unbinder = ButterKnife.bind(this);
        db = sqlBrite.wrapDatabaseHelper(new DbOpenHelper(this),Schedulers.io());
        setSupportActionBar(toolbar);
        subject = PublishSubject.create();
        initDayPath();
        subscription = new CompositeSubscription();
        adapter = new GankDailyAdapter(contentList,this);
        content.setHasFixedSize(true);
        linearlayoutManager = new LinearLayoutManager(this);
        content.setLayoutManager(linearlayoutManager);
        content.setAdapter(adapter);
        subscription.add(db.createQuery(Db.TODAY_TABLE_NAME,"select * from "+Db.TODAY_TABLE_NAME)
                .mapToOne(GankDaily.parse)
                .filter(Funcs.not(Results.isNull()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(GankTodaySuccess));
        Observable<Result<GankRepositories<GankDaily>>> result = subject
                .concatMap(getGank)
                .observeOn(AndroidSchedulers.mainThread())
                .share();

        subscription.add(result.filter(Funcs.not(Results.isSuccessful()))
                .subscribe(gankTodayError));
        //检测网络异常

        Observable<GankDaily> returnData = result.
                filter(Results.isSuccessful())
                .map(gankRepositoriesResult -> gankRepositoriesResult.response().body().results);

        Observable<GankDaily> postData = returnData.filter(Funcs.not(Results.isNull()));

        returnData.filter(Results.isNull())
                .subscribe(gankTodayNotUpdate);

        subscription.add(postData
                .filter(daily -> !daily.equals(temp))
                .observeOn(Schedulers.io())
                .subscribe(dataInsert));
        //由于gank的每日的更新的item不确定所以对应的id更新的数据库的想法有一点的不合实际
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment fragment = DatePickerDialogFragment.Instance(day);
                fragment.show(getSupportFragmentManager(),"DatePickerfragment");
            }
        });
        subject.onNext(day);
    }

    @Override
    public void setDateSet(int year, int month, int dayOfMonth) {
        day.setDay(dayOfMonth)
                .setMonth(month)
                .setYear(year);
        subject.onNext(day);
    }

    private void initDayPath(){
        day = new GankDay(Util.getDate(new Date()));
    }

    private final Action1<GankDaily> dataInsert = new Action1<GankDaily>() {
        @Override
        public void call(GankDaily gankDaily) {
            Log.e("gankDaily","not null");
            BriteDatabase.Transaction transaction = db.newTransaction();
            try{
                db.execute("delete From "+Db.TODAY_TABLE_NAME+" where "+ GankInfoContract.GankEntry._ID+" > -1");
                List<GankInfo> infoList = GanKDailyToList(gankDaily);
                for (GankInfo info:infoList){
                    db.insert(Db.TODAY_TABLE_NAME,new GankInfo.Builder(info).build());
                }
                transaction.markSuccessful();
            }finally {
                transaction.end();
            }
        }
    };

    private final Func1<GankDay,Observable<Result<GankRepositories<GankDaily>>>> getGank =
            new Func1<GankDay, Observable<Result<GankRepositories<GankDaily>>>>() {
        @Override
        public Observable<Result<GankRepositories<GankDaily>>> call(GankDay day) {
            return RetrofitHelper.getInstance().getGank().gankDailyInfo(
                    day.getYear(),day.getMonthForGank(),day.getDay())
                    .subscribeOn(Schedulers.io());
        }
    };

    //这里可以通过多播来实现了图片和adapter的加载 但是意义不大所以改为了这种方式
    private final Action1<GankDaily> GankTodaySuccess = new Action1<GankDaily>() {
        @Override
        public void call(GankDaily gankDaily) {
            temp = gankDaily;
            List<GankInfo> list = GanKDailyToList(gankDaily);
            contentList.clear();
            contentList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    };


    private final Action1<Result<GankRepositories<GankDaily>>> gankTodayError = new Action1<Result<GankRepositories<GankDaily>>>() {
        @Override
        public void call(Result<GankRepositories<GankDaily>> gankRepositoriesResult) {
            if (gankRepositoriesResult.isError()){
                Log.e("network","TAG");
            }else {
                Log.e("response code",gankRepositoriesResult.response().code()+"");
                //可以处理不同的status code
            }
            if (temp==null) {
                //简单粗暴
                textView.setVisibility(View.VISIBLE);
            }
        }
    };

    private final Action1<GankDaily> gankTodayNotUpdate = new Action1<GankDaily>() {
        @Override
        public void call(GankDaily gankDaily) {
            TipDialogFragment HasGankFragment = TipDialogFragment.newInstance("今天暂时没有干货啦");
            HasGankFragment.show(getSupportFragmentManager(),"NO_GANK");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        subscription.clear();
    }

    private List<GankInfo> GanKDailyToList(GankDaily daily){
        List<GankInfo> list = new ArrayList<>();
        if (daily.Meizi!=null){
            list.addAll(daily.Meizi);
        }
        if (daily.Android!=null){
            list.addAll(daily.Android);
        }
        if (daily.iOS!=null){
            list.addAll(daily.iOS);
        }
        if (daily.Fontend!=null){
            list.addAll(daily.Fontend);
        }
        if (daily.Recommd!=null){
            list.addAll(daily.Recommd);
        }
        if (daily.Resources!=null){
            list.addAll(daily.Resources);
        }
        if (daily.Video!=null){
            list.addAll(daily.Video);
        }
        return list;
    }
}
