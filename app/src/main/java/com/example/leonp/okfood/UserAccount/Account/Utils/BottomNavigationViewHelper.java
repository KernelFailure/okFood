package com.example.leonp.okfood.UserAccount.Account.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.MainActivity.MainActivity;
import com.example.leonp.okfood.UserAccount.Account.Matcher.MatcherActivity;
import com.example.leonp.okfood.UserAccount.Account.Profile.ProfileActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by leonp on 4/21/2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Starting");

        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }


    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view) {
        Log.d(TAG, "enableNavigation: Starting");

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.matcher:
                        Toast.makeText(context, "Matcher Selected", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(context, MatcherActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.feed:
                        Toast.makeText(context, "Feed selected", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(context, MainActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.profile:
                        Toast.makeText(context, "Profile selected", Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent3);
                        break;
                }

                return false;
            }
        });

    }

}
