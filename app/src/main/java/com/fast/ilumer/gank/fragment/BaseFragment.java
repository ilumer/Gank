package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by root on 10/16/16.
 * -
 */

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    View mRootView;
    protected abstract int getLayoutId();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null==mRootView){
            mRootView = inflater.inflate(getLayoutId(),container,false);
        }
        unbinder= ButterKnife.bind(this,mRootView);
        return mRootView;
    }

    protected Unbinder getUnbinder(){
        return this.unbinder;
    }
}
