package com.example.leonp.okfood.UserAccount.Account.PublishPost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.Utils.SectionsPagerAdapter;

public class PublishPostActivity extends AppCompatActivity {

    private static final String TAG = "PublishPostActivity";

    // widgets
    private ImageView btnCancel;
    private ViewPager viewpager_container;
    private TextView tvCount;
    private ImageView dot1, dot2, dot3;
    private ImageView dotLight1, dotLight2, dotLight3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishpost);

        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        viewpager_container = (ViewPager) findViewById(R.id.viewpager_container);
        tvCount = (TextView) findViewById(R.id.tvCount);
        dot1 = (ImageView) findViewById(R.id.dot1);
        dot2 = (ImageView) findViewById(R.id.dot2);
        dot3 = (ImageView) findViewById(R.id.dot3);
        dotLight1 = (ImageView) findViewById(R.id.dotLight1);
        dotLight2 = (ImageView) findViewById(R.id.dotLight2);
        dotLight3 = (ImageView) findViewById(R.id.dotLight3);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked to cancel activity");
                finish();
            }
        });

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TakePictureFragment());
        adapter.addFragment(new AddIngredientsFragment());
        adapter.addFragment(new AddInstructionsFragment());

        viewpager_container.setAdapter(adapter);
        viewpager_container.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: Trying to change text view to: " + position + 1);

                position++;
                tvCount.setText("" + position);
                switch (position) {
                    case 1:
                        dotLight1.setVisibility(View.INVISIBLE);

                        dotLight2.setVisibility(View.VISIBLE);
                        dotLight3.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        dotLight2.setVisibility(View.INVISIBLE);

                        dotLight1.setVisibility(View.VISIBLE);
                        dotLight3.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        dotLight3.setVisibility(View.INVISIBLE);

                        dotLight1.setVisibility(View.VISIBLE);
                        dotLight2.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setDot(int page) {
        Log.d(TAG, "setDot: Setting dot for page: " + page);


    }
}
