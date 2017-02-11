package com.fast.ilumer.gank;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fast.ilumer.gank.activity.GankTodayActivity;
import com.fast.ilumer.gank.activity.SearchActivity;
import com.fast.ilumer.gank.dao.DbOpenHelper;
import com.fast.ilumer.gank.fragment.GankMeiZiFragment;
import com.fast.ilumer.gank.fragment.GankTypeFragment;
import com.fast.ilumer.gank.fragment.RecyclerViewFragment;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.InstanceDb{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindArray(R.array.various_type)
    String[] titles;
    private SqlBrite sqlBrite = new SqlBrite.Builder().build();
    private BriteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        db = sqlBrite.wrapDatabaseHelper(new DbOpenHelper(this), Schedulers.io());
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        mViewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(),titles));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        //http://stackoverflow.com/questions/29540724/start-new-activity-from-searchview
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.today_gank:{
                Intent i = new Intent(this, GankTodayActivity.class);
                startActivity(i);
                return true;
            }
            case R.id.action_search:{
                Intent i = new Intent(this, SearchActivity.class);
                startActivity(i);
                return true;
            }

            case R.id.setting:{
                return true;
            }


            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public BriteDatabase instance() {
        return db;
    }

    public static class ViewPageAdapter extends FragmentStatePagerAdapter {
        //不同的viewpager的adapter的区别
        //FragmentStatePagerAdapter 会将fragment实例销毁所以需要layoutstate销毁掉
        //FragmentPagerAdapter 不会销毁实例 但是会销毁视图
        //http://stackoverflow.com/questions/18747975/difference-between-fragmentpageradapter-and-fragmentstatepageradapter
        private final String[] titles;

        private ViewPageAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.titles = titles;
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
                    return GankMeiZiFragment.newInstance(titles[6]);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
