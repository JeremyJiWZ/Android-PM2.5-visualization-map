package com.andy.dbmz;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 2015/6/11
 */
public class MainActivity extends AppCompatActivity {

    private static final String[] tabTitles = new String[]{"今日头条", "网络监测",
             "饮食运动", "PM2.5科普", "疾病防护"};
    private static final String[] tabIds = new String[]{"13", "21", "19", "20","18"};
    //private static final String[] tabIds = new String[]{"2", "6", "7", "3","4"};

    private Toolbar mToolBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mTabLayout.setupWithViewPager(mViewPager);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_statement:
                Snackbar.make(mToolBar, "本APP仅为学习开发使用,所有图片抓取自http://www.dbmeinv.com/,版权归原作者所有", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;

            case R.id.action_about:
                Snackbar.make(mToolBar, "浙江大学计算机学院2016", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {

        int mCount = tabTitles.length;
        for (int i = 0; i < mCount; i++) {
            if (i==1)
                adapter.addFragment(MapFragment.newInstance(),tabTitles[i]);
            else
                adapter.addFragment(PageSectionFragment.newInstance(tabIds[i]), tabTitles[i]);
            //如果要修改显示部分，要从这里修改，放进去的不应该是PageSectionFragment类型。
        }
        viewPager.setAdapter(adapter);
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        PageSectionFragment psf=(PageSectionFragment)adapter.getItem(mViewPager.getCurrentItem());
        if(!psf.checkWebView()){
             super.onBackPressed();
        }
        return;
    }
}
