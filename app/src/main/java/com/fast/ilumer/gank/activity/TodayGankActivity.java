package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.fragment.TipDialogFragment;
import com.fast.ilumer.gank.model.GankDaily;
import com.fast.ilumer.gank.model.GankDailyAdapter;
import com.fast.ilumer.gank.model.GankInfo;
import com.fast.ilumer.gank.model.GankRepositories;
import com.fast.ilumer.gank.network.Results;
import com.fast.ilumer.gank.network.RetrofitHelper;
import com.fast.ilumer.gank.rx.Funcs;
import com.fast.ilumer.gank.widget.RatioImageView;

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

public class TodayGankActivity extends AppCompatActivity {

    private CompositeSubscription subscription;
    private GankDailyAdapter adapter;
    private LinearLayoutManager linearlayoutManager;
    private BottomSheetBehavior behavior;
    private List<GankInfo> contentList = new ArrayList<>();
    private  boolean hasGank ;
    @BindView(R.id.gril)
    ImageView gril;
    @BindView(R.id.content)
    RecyclerView content;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;
    Date currentDate= new Date();
    DayPath dayPath = new DayPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_gank);
        unbinder = ButterKnife.bind(this);
        ((RatioImageView) gril).setRatio(0.818f);
        setSupportActionBar(toolbar);
        behavior = BottomSheetBehavior.from(content);
        initDayPath();
        subscription = new CompositeSubscription();
        adapter = new GankDailyAdapter(contentList,this);
        content.setHasFixedSize(true);
        linearlayoutManager = new LinearLayoutManager(this);
        content.setLayoutManager(linearlayoutManager);
        content.setAdapter(adapter);
        Observable<Result<GankRepositories<GankDaily>>> result = Observable.just(dayPath)
                .concatMap(getGank)
                .observeOn(AndroidSchedulers.mainThread())
                .share();

        result.filter(Funcs.not(Results.isSuccessful()))
                .subscribe(GankTodayError);
        //检测网络异常

        Observable<GankDaily> returnData = result.
                filter(Results.isSuccessful())
                .map(new Func1<Result<GankRepositories<GankDaily>>, GankDaily>() {
            @Override
            public GankDaily call(Result<GankRepositories<GankDaily>> gankRepositoriesResult) {
                return gankRepositoriesResult.response().body().results;
            }
        });

        Observable<GankDaily> postData = returnData.filter(Results.ISNULL());

        returnData.filter(Funcs.not(Results.ISNULL()))
                .subscribe(GankTodayUpdate);

        subscription.add(postData.filter(new Func1<GankDaily, Boolean>() {
            @Override
            public Boolean call(GankDaily gankDaily) {
                return gankDaily.Meizi!=null;
            }
        }).map(new Func1<GankDaily, GankInfo>() {
            @Override
            public GankInfo call(GankDaily gankDaily) {
                return gankDaily.Meizi.get(0);
            }
        })
                .subscribe(new Action1<GankInfo>() {
                    @Override
                    public void call(GankInfo info) {
                        Glide.with(TodayGankActivity.this)
                                .load(info.getUrl())
                                .centerCrop()
                                .into(gril);
                    }
                }));
        subscription.add(postData.map(mapList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<GankInfo>>() {
                    @Override
                    public void call(List<GankInfo> list) {
                        contentList.addAll(list);
                        adapter.notifyItemRangeInserted(0,list.size());
                    }
                }));
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


    private final Func1<DayPath,Observable<Result<GankRepositories<GankDaily>>>> getGank =
            new Func1<DayPath, Observable<Result<GankRepositories<GankDaily>>>>() {
        @Override
        public Observable<Result<GankRepositories<GankDaily>>> call(DayPath dayPath) {
            return RetrofitHelper.getInstance().getGank().GankDailyInfo(dayPath.year,dayPath.month,dayPath.day)
                    .subscribeOn(Schedulers.io());
        }
    };

    private final Func1<GankDaily,List<GankInfo>> mapList = new Func1<GankDaily, List<GankInfo>>() {
        @Override
        public List<GankInfo> call(GankDaily gankDaily) {
            List<GankInfo> list = new ArrayList<>();
            if (gankDaily.Android!=null){
                list.addAll(gankDaily.Android);
            }
            if (gankDaily.iOS!=null){
                list.addAll(gankDaily.iOS);
            }
            if (gankDaily.Fontend!=null){
                list.addAll(gankDaily.Fontend);
            }
            if (gankDaily.Recommd!=null){
                list.addAll(gankDaily.Recommd);
            }
            if (gankDaily.Resources!=null){
                list.addAll(gankDaily.Resources);
            }
            if (gankDaily.Video!=null){
                list.addAll(gankDaily.Video);
            }
            return list;
        }
    };

    private final Action1<Result<GankRepositories<GankDaily>>> GankTodayError = new Action1<Result<GankRepositories<GankDaily>>>() {
        @Override
        public void call(Result<GankRepositories<GankDaily>> gankRepositoriesResult) {
            if (gankRepositoriesResult.isError()){
                Log.e("network","TAG");
            }
        }
    };

    private final Action1<GankDaily> GankTodayUpdate = new Action1<GankDaily>() {
        @Override
        public void call(GankDaily gankDaily) {

            if (hasGank){
                TipDialogFragment HasGankFragment = TipDialogFragment.newInstance("干货还没有更新啦");
                HasGankFragment.show(getSupportFragmentManager(),"yes");
            }else {
                TipDialogFragment HasGankFragment = TipDialogFragment.newInstance("今天没有干货啦");
                HasGankFragment.show(getSupportFragmentManager(),"yes");
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        subscription.clear();
    }

    public static class DayPath{
        int year;
        int month;
        int day;
    }
}
