package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureActivity extends AppCompatActivity {

    @BindView(R.id.gril)
    SimpleDraweeView gril;
    @BindView(R.id.toolbar)

    Toolbar mtoolbar;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        setSupportActionBar(mtoolbar);
        uri = getIntent().getStringExtra("uri");
        mtoolbar.setTitle("PictureActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        gril.setImageURI(uri);
    }
}
