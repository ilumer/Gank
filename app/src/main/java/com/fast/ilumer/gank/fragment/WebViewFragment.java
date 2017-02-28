package com.fast.ilumer.gank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(getString(R.string.app_name));
        String uri = getArguments().getString(EXTRA_URL);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }else {
                    progressBar.setProgress(newProgress);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(getActivity(),"no error",Toast.LENGTH_SHORT).show();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(uri);
        //:TODO 添加progressbar
    }

    @Override
    public void onDestroy() {
        if (webView!=null){
            webView.destroy();
            webView.destroy();
        }
        super.onDestroy();

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
