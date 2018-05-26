package com.example.leonp.okfood.UserAccount.Account.Matcher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.MainActivity.MainActivity;
import com.example.leonp.okfood.UserAccount.Account.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by leonp on 4/22/2018.
 */

public class MatcherActivity extends AppCompatActivity {

    private static final String TAG = "MatcherActivity";

    // Constants
    private static final int ACTIVITY_NUM = 0;

    // vars
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matcher);

        mContext = MatcherActivity.this;

        setupBottomNavigationView();
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
