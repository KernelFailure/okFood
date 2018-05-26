package com.example.leonp.okfood.UserAccount.Account.Comments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.leonp.okfood.R;

/**
 * Created by leonp on 4/28/2018.
 */

public class CommentThreadActivity extends AppCompatActivity {
    private static final String TAG = "CommentThreadActivity";
    
    // widgets
    private TextView tvPostTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentthread);

        Log.d(TAG, "onCreate: Attempting to set post title");
        tvPostTitle = (TextView) findViewById(R.id.tvPostTitle);
        
        String post_title = getIntent().getStringExtra("post_title");
        tvPostTitle.setText(post_title);
    }
}
