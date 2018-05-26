package com.example.leonp.okfood.UserAccount.Account.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.Models.Post;
import com.example.leonp.okfood.UserAccount.Account.PublishPost.PublishPostActivity;
import com.example.leonp.okfood.UserAccount.Account.Utils.FeedListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by leonp on 4/22/2018.
 */

public class FeedFragment extends Fragment {
    private static final String TAG = "FeedFragment";

    // widgets
    private ListView mListView;
    private SwipeRefreshLayout swipe;
    private FloatingActionButton fabCreateNewRecipe;

    // vars
    private FeedListAdapter mAdapter;
    private ArrayList<Post> mPostList;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        Log.d(TAG, "onCreateView: Starting");
        mListView = (ListView) view.findViewById(R.id.listView);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        fabCreateNewRecipe = (FloatingActionButton) view.findViewById(R.id.fabCreateNewRecipe);
        mPostList = new ArrayList<>();
        //mContext = Context;
        mAdapter = new FeedListAdapter(getActivity(), R.layout.layout_feed_listitem, mPostList);

        SwipeRefreshLayout.OnRefreshListener mListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Tried to Refresh", Toast.LENGTH_SHORT).show();
                getPostsFromDatabase();
            }
        };

        swipe.setOnRefreshListener(mListener);



        fabCreateNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Clicked on FAB", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
                Log.d(TAG, "onClick: Just to make sure...the FAB button was just struck");
                Intent intent = new Intent(getContext(), PublishPostActivity.class);
                startActivity(intent);
            }
        });


        getPostsFromDatabase();

        //displayPosts();

        return view;
    }

    private void getPostsFromDatabase() {
        Log.d(TAG, "getPostsFromDatabase: Starting");
        mPostList.clear();
        mListView.setVisibility(View.INVISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("posts");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: Found a snapshot");
                    Post tempPost = new Post();
                    tempPost = singleSnapshot.getValue(Post.class);
                    Log.d(TAG, "onDataChange: Added Post: " + tempPost.getTitle());
                    mPostList.add(tempPost);
                    mAdapter.notifyDataSetChanged();
                }
                Log.d(TAG, "onDataChange: Post List size: " + mPostList.size());

                mListView.setVisibility(View.VISIBLE);

                displayPosts();
                swipe.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        //reference.addValueEventListener(mListener);
    }
    
    ValueEventListener mListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(TAG, "onDataChange: Value Event Listener Changed");
            //getPostsFromDatabase();
            mAdapter.notifyDataSetChanged();
            //displayPosts();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private void displayPosts() {
        Log.d(TAG, "displayPosts: Starting");


        // setup listview
        Log.d(TAG, "displayPosts: Post List size: " + mPostList.size());

        try {
            
            for (int i = 0; i < mPostList.size(); i++) {
                Log.d(TAG, "displayPosts: Current Title and Position: " 
                        + mPostList.get(i).getTitle() + " (" + mPostList.get(i).getPosition() + ")");
            }

//            Collections.sort(mPostList, new Comparator<Post>() {
//                @Override
//                public int compare(Post post, Post t1) {
//                    return t1.getTitle().compareTo(post.getTitle());
//                }
//            });
//
//            Log.d(TAG, "displayPosts: Second For Loop starting now");
//            for (int i = 0; i < mPostList.size(); i++) {
//                Log.d(TAG, "displayPosts: Current Title and Position: "
//                        + mPostList.get(i).getTitle() + " (" + mPostList.get(i).getPosition() + ")");
//            }

            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Post clickedPost = mPostList.get(i);
                    String clickedTitle = clickedPost.getTitle();
                    Log.d(TAG, "onItemClick: Title of clicked post: " + clickedTitle);
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "displayPosts: NullPointerException: " + e.getMessage());
        }

    }

}
