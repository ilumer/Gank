package com.fast.ilumer.gank.model.viewholder;

import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.Util;
import com.fast.ilumer.gank.model.GankInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 12/24/16.
 */

public class GirlHolder extends BaseHolder{
    @BindView(R.id.image)
    public SimpleDraweeView imageView;
    private float aspectRatio;
    public GirlHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindModel(GankInfo info) {
        super.bindModel(info);
        imageView.setImageURI(Util.meiZiImageParse(info.getUrl(),aspectRatio));
        imageView.setAspectRatio(aspectRatio);
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        imageView.setImageBitmap(null);
    }

    public void setAspectRatio(float aspectRatio){
        this.aspectRatio = aspectRatio;
    }
}
