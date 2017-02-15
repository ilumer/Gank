package com.fast.ilumer.gank.model.viewholder;

import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 12/26/16.
 */

public class InfoPicHolder extends InfoHolder{
    @Nullable@BindView(R.id.imageView)
    SimpleDraweeView imageView;
    @BindColor(R.color.transparent_dark)
    int transparentDark;
    @BindColor(android.R.color.white)
    int white;

    public InfoPicHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        bottomBackground.setBackgroundColor(transparentDark);
        mDesc.setTextColor(white);
        mDesc.setMaxLines(1);
        mRecommender.setTextColor(white);
        mPublishDate.setTextColor(white);
    }

    @Override
    public void bindModel(GankInfo item) {
        super.bindModel(item);
        if (imageView!=null) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(item.getImages().get(0))
                    .setAutoPlayAnimations(true)
                    .build();
            imageView.setController(controller);
        }
    }

    @Override
    public void onAttach() {
        super.onAttach();
        if (imageView.getController().getAnimatable()!=null) {
            imageView.getController().getAnimatable().start();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (imageView.getController().getAnimatable()!=null) {
            imageView.getController().getAnimatable().stop();
        }
    }

}
