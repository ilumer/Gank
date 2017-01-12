package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.DbOpenHelper;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.fast.ilumer.gank.fragment.TipDialogFragment;
import com.fast.ilumer.gank.model.GankDaily;
import com.fast.ilumer.gank.model.GankDailyAdapter;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.GankRepositories;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.rx.Funcs;
import com.fast.ilumer.gank.rx.Results;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.Calendar;
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
import rx.subscriptions.CompositeSubscription;

public class GankTodayActivity extends AppCompatActivity {

    private CompositeSubscription subscription;
    private GankDailyAdapter adapter;
    private LinearLayoutManager linearlayoutManager;
    private List<GankInfo> contentList = new ArrayList<>();
    private  boolean hasGank ;
    private SqlBrite sqlBrite = new SqlBrite.Builder().build();
    private BriteDatabase db ;
    private GankDaily temp;
    @BindView(R.id.content)
    RecyclerView content;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.empty)
    TextView textView;
    Unbinder unbinder;
    Date currentDate= new Date();
    DayPath dayPath = new DayPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_gank);
        unbinder = ButterKnife.bind(this);
        db = sqlBrite.wrapDatabaseHelper(new DbOpenHelper(this),Schedulers.io());
        setSupportActionBar(toolbar);
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
        Observable<Result<GankRepositories<GankDaily>>> result = Observable.just(dayPath)
                .concatMap(getGank)
                .observeOn(AndroidSchedulers.mainThread())
                .share();

        subscription.add(result.filter(Funcs.not(Results.isSuccessful()))
                .subscribe(GankTodayError));
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
    }


    private void initDayPath(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        dayPath.year = cal.get(Calendar.YEAR);
        dayPath.month = cal.get(Calendar.MONTH)+1;
        //http://stackoverflow.com/questions/344380/why-is-january-month-0-in-java-calendar
        dayPath.day = cal.get(Calendar.DAY_OF_MONTH);
        int number = cal.get(Calendar.DAY_OF_WEEK);
        hasGank = number<7&&number>1;
    }

    private final Action1<GankDaily> dataInsert = new Action1<GankDaily>() {
        @Override
        public void call(GankDaily gankDaily) {
            Log.e("gankDaily","not null");
            BriteDatabase.Transaction transaction = db.newTransaction();
            try{
                db.execute("delete From "+Db.TODAY_TABLE_NAME+" where "+ GankInfoContract.GankEntry._ID+" > -1");
                db.insert(Db.TODAY_TABLE_NAME,new GankInfo.Builder(gankDaily.Meizi.get(0)).build());
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

    private final Func1<DayPath,Observable<Result<GankRepositories<GankDaily>>>> getGank =
            new Func1<DayPath, Observable<Result<GankRepositories<GankDaily>>>>() {
        @Override
        public Observable<Result<GankRepositories<GankDaily>>> call(DayPath dayPath) {
            return RetrofitHelper.getInstance().getGank().gankDailyInfo(dayPath.year,dayPath.month,dayPath.day)
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


    private final Action1<Result<GankRepositories<GankDaily>>>   GankTodayError = new Action1<Result<GankRepositories<GankDaily>>>() {
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

            if (hasGank){
                TipDialogFragment HasGankFragment = TipDialogFragment.newInstance("干货还没有更新啦");
                HasGankFragment.show(getSupportFragmentManager(),"UPDATE_GANK");
            }else {
                TipDialogFragment HasGankFragment = TipDialogFragment.newInstance("今天没有干货啦");
                HasGankFragment.show(getSupportFragmentManager(),"NO_GANK");
            }
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

    public static class DayPath{
        int year;
        int month;
        int day;
    }
}
