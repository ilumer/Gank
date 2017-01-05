package com.fast.ilumer.gank.model.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.model.GankInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 12/26/16.
 */

public class InfoHolder extends BaseHolder{
    @BindView(R.id.bottom_background)
    public LinearLayout bottomBackground;
    @BindView(R.id.description)
    TextView mDesc;
    @BindView(R.id.recommender)
    TextView mRecommender;
    @BindView(R.id.publishdate)
    TextView mPublishDate;
    @BindString(R.string.referrer)
    String referFormat;

    public InfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindModel(GankInfo info) {
        mDesc.setText(info.getDesc());
        if (info.getWho()!=null) {
            mRecommender.setText(String.format(referFormat,info.getWho()));
        }
        mPublishDate.setText(formatDate(info.getPublishedAt()));
    }

    public void onAttach(){

    }

    public void onDetach(){

    }

    private static CharSequence formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dateFormat.format(date);
    }
}
