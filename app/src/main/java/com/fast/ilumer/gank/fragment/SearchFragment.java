package com.fast.ilumer.gank.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.activity.SearchResultActivity;
import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.GankInfoContract;
import com.fast.ilumer.gank.model.SearchRepo;
import com.fast.ilumer.gank.model.SuggestAdapter;
import com.fast.ilumer.gank.model.listener.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by ilumer on 1/7/17.
 */

public class SearchFragment extends BaseFragment implements OnClickListener{
    @BindView(R.id.search_suggestion)
    RecyclerView mSearchSuggestion;
    @BindView(R.id.search_bar)
    SearchView mSearchView;
    List<SearchRepo> mSuggestionList = new ArrayList<>();
    SuggestAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new SuggestAdapter(mSuggestionList);
        mAdapter.setOnClicklistener(this);
        mSearchSuggestion.setAdapter(mAdapter);
        mSearchSuggestion.setLayoutManager(mLinearLayoutManager);
        setUpSearchView();
        searchFor();
    }

    private void setUpSearchView(){
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setIconifiedByDefault(false);
        //searchView expand
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    searchFor();
                }else {
                    searchFor(newText);
                }
                return true;
            }
        });

    }

    private void searchFor(){
        subscription.add(Observable.fromCallable(() ->
                getActivity().getContentResolver().query(
                        GankInfoContract.GankEntry.TYPE_CONTENT_URI,
                        new String[]{"Distinct("+GankInfoContract.GankEntry.TYPE+")"},
                        GankInfoContract.GankEntry.TYPE+" != ?",
                        new String[]{"福利"},
                        null
                )
        ).map(cursor -> {
            cursor.moveToFirst();
            List<SearchRepo> mlist = new ArrayList<>();
            try {
                do {
                    SearchRepo repo = new SearchRepo();
                    repo.setShowItem(Db.getString(cursor, GankInfoContract.GankEntry.TYPE));
                    repo.setTag(SearchTag.type);
                    mlist.add(repo);
                } while (cursor.moveToNext());
            }finally {
                cursor.close();
            }
            return mlist;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter));
    }

    private void searchFor(String query) {
        subscription.add(Observable.fromCallable(new Func0<Cursor>() {
            @Override
            public Cursor call() {
                return getActivity()
                        .getContentResolver()
                        .query(GankInfoContract.GankEntry.TYPE_CONTENT_URI,
                                new String[]{GankInfoContract.GankEntry.DESC, GankInfoContract.GankEntry.URL},
                                "type != ? and description like ? ",
                                new String[]{"福利", "%" + query + "%"},
                                null,
                                null);
            }
                /*http://stackoverflow.com/questions/5179563/sqlite-like-problem-in-android*/
        }).map(cursor -> {
            List<SearchRepo> mlist = new ArrayList<>();
            try {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    SearchRepo repo = new SearchRepo();
                    repo.setShowItem(Db.getString(cursor, GankInfoContract.GankEntry.DESC));
                    repo.setUri(Db.getString(cursor, GankInfoContract.GankEntry.URL));
                    repo.setTag(SearchTag.item);
                    mlist.add(repo);
                }
            }finally {
                cursor.close();
            }
            return mlist;
        })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter));
    }

    @Override
    public void OnClick(SearchRepo repo) {
        if (repo.getTag()==SearchTag.type){
            Intent i = new Intent(getActivity(), SearchResultActivity.class);
            i.putExtra(SearchResultActivity.EXTRA_STRING,repo.getShowItem());
            getActivity().startActivity(i);
        }else if (repo.getTag()==SearchTag.item){
            Util.getCustomIntent(getActivity())
                    .build()
                    .launchUrl(getActivity(), Uri.parse(repo.getUri()));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.search_fragment;
    }

    public interface SearchTag{
        int type = 0x1;
        int item = 0x2;
    }
}
