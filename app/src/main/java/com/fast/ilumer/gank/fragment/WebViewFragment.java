package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.fast.ilumer.gank.R;

import butterknife.BindView;

/**
 * Created by ilumer on 17-2-22.
 *并没有使用webkit包下面的WebViewFragment
 */

public class WebViewFragment extends BaseFragment{
    public static final String EXTRA_URL = "WebViewFragment.extra_url";

    @BindView(R.id.webview)
    WebView webView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String uri = getArguments().getString(EXTRA_URL);
        webView.loadUrl(uri);
        //:TODO 添加progressbar
    }

    public static WebViewFragment newInstance(String Uri){
        Bundle arg = new Bundle();
        arg.putString(EXTRA_URL,Uri);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(arg);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.webview_fragment;
    }
}
