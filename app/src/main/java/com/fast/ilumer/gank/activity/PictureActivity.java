package com.fast.ilumer.gank.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fast.ilumer.gank.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureActivity extends AppCompatActivity {

    @BindView(R.id.gril)
    ImageView gril;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        uri = getIntent().getStringExtra("uri");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(gril);
    }
}
