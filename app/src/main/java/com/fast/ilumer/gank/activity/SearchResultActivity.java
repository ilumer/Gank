package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.dao.DbOpenHelper;
import com.fast.ilumer.gank.fragment.GankTypeFragment;
import com.fast.ilumer.gank.fragment.RecyclerViewFragment;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by ${ilumer} on 2/2/17.
 */

public class SearchResultActivity extends BaseActivity implements RecyclerViewFragment.DbInstance {
    public static final String EXTRA_STRING = "SearchResultActivity.Type";
    private SqlBrite sqlBrite = new SqlBrite.Builder().build();
    private BriteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra(EXTRA_STRING));
        db = sqlBrite.wrapDatabaseHelper(new DbOpenHelper(this), Schedulers.io());
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.toolbar_base_layout;
    }

    @Override
    public Fragment createNewFragment() {
        String type = getIntent().getStringExtra(EXTRA_STRING);
        return GankTypeFragment.newInstance(type);
    }

    @Override
    public BriteDatabase instance() {
        return db;
    }
}
