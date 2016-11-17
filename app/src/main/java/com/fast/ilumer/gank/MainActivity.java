package com.fast.ilumer.gank;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fast.ilumer.gank.fragment.GankMeiZiFragment;
import com.fast.ilumer.gank.fragment.GankTypeFragment;
import com.fast.ilumer.gank.rx.RxBus;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindArray(R.array.various_type)
    String[] titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        mViewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(),titles,RxBus.getDefault()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class ViewPageAdapter extends FragmentStatePagerAdapter {
        private final String[] titles;
        private RxBus bus;

        private ViewPageAdapter(FragmentManager fm, String[] titles, RxBus bus) {
            super(fm);
            this.titles = titles;
            this.bus = bus;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return GankTypeFragment.newInstance(titles[0]);
                case 1:
                    return GankTypeFragment.newInstance(titles[1]);
                case 2:
                    return GankTypeFragment.newInstance(titles[2]);
                case 3:
                    return GankTypeFragment.newInstance(titles[3]);
                case 4:
                    return GankTypeFragment.newInstance(titles[4]);
                case 5:
                    return GankTypeFragment.newInstance(titles[5]);
                case 6:
                    return new GankMeiZiFragment();
                case 7:
                    return GankTypeFragment.newInstance(titles[7]);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
