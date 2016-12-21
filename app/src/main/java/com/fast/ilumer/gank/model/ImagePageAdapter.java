package com.fast.ilumer.gank.model;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fast.ilumer.gank.R;

import java.util.List;

/**
 * Created by root on 12/9/16.
 *
 */

public class ImagePageAdapter extends PagerAdapter {

    private List<String> imageList;

    public ImagePageAdapter(List<String> imageList) {
        this.imageList=imageList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_holder,container,false);
        Uri uri = Uri.parse(imageList.get(position));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        SimpleDraweeView v = (SimpleDraweeView) view.findViewById(R.id.image);
        v.setController(controller);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
