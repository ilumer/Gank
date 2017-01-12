package com.fast.ilumer.gank.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.SuggestAdapter;

import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * Created by root on 1/7/17.
 */

public class SearchFragment extends BaseFragment {
    @BindView(R.id.search_suggestion)
    RecyclerView mSearchSuggestion;
    @BindView(R.id.search_bar)
    SearchView mSearchView;
    @BindArray(R.array.various_type)
    String[] mSuggestionList;
    SuggestAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new SuggestAdapter(Arrays.asList(mSuggestionList));
        mSearchSuggestion.setAdapter(mAdapter);
        mSearchSuggestion.setLayoutManager(mLinearLayoutManager);
        setUpSearchView();
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
