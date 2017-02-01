package com.fast.ilumer.gank.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.fast.ilumer.gank.model.SuggestAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/7/17.
 */

public class SearchFragment extends BaseFragment {
    @BindView(R.id.search_suggestion)
    RecyclerView mSearchSuggestion;
    @BindView(R.id.search_bar)
    SearchView mSearchView;
    List<String> mSuggestionList = new ArrayList<>();
    SuggestAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new SuggestAdapter(mSuggestionList);
        mSearchSuggestion.setAdapter(mAdapter);
        mSearchSuggestion.setLayoutManager(mLinearLayoutManager);
        setUpSearchView();
        Observable.fromCallable(new Callable<Cursor>() {
            @Override
            public Cursor call() throws Exception {
                return getActivity().getContentResolver().query(
                        GankInfoContract.GankEntry.CONTENT_URI,
                        new String[]{"Distinct("+GankInfoContract.GankEntry.TYPE+")"},
                        null,
                        null,
                        null
                        );
            }
        }).map(cursor -> {
            cursor.moveToFirst();
            List<String> mlist = new ArrayList<>();
            do {
                mlist.add(Db.getString(cursor,GankInfoContract.GankEntry.TYPE));
            }while (cursor.moveToNext());
            return mlist;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter);
    }

    private void setUpSearchView(){
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }

    private void searchFor(String query){

    }

    @Override
    protected int getLayoutId() {
        return R.layout.search_fragment;
    }
}
