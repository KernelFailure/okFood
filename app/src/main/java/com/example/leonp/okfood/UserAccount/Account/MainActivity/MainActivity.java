package com.example.leonp.okfood.UserAccount.Account.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.Utils.BottomNavigationViewHelper;
import com.example.leonp.okfood.UserAccount.Account.Utils.SectionsPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by leonp on 4/18/2018.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Constants
    private static final int ACTIVITY_NUM = 1;

    // widgets
    private FrameLayout mFrameLayout;
    private TabLayout tabLayout;
    private ViewPager mViewPager;

    // vars
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager();

        //setupBottomNavigationView();
    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: Starting");

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Log.d(TAG, "setupViewPager: created adapter.  Should be length zero\nActual length: " + adapter.getCount());
        adapter.addFragment(new MatcherFragment());
        adapter.addFragment(new FeedFragment());
        adapter.addFragment(new ProfileFragment());
        Log.d(TAG, "setupViewPager: finished filling adapter.  should be length 3\nActual length: " + adapter.getCount());
        mViewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_matcher_offcolor);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_feed_offcolor);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_profile_offcolor);

        Log.d(TAG, "setupViewPager: Finished");

    }

    public void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: Starting");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
