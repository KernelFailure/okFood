package com.example.leonp.okfood.UserAccount.Account.CreateNewPost;


import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.leonp.okfood.R;

/**
 * Created by leonp on 4/25/2018.
 */

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    // widgets
    private TextView tvNext;
    private Spinner spinner;
    private ImageView ivCancel, ivCenterImage;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        tvNext = (TextView) view.findViewById(R.id.tvNext);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ivCancel = (ImageView) view.findViewById(R.id.ivCancel);
        ivCenterImage = (ImageView) view.findViewById(R.id.ivCenterImage);
        gridView = (GridView) view.findViewById(R.id.gridView);
        
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to create post activity ");
                getActivity().finish();    // this is wrong. It closes the entire create post activity when is should
                                            // just close the gallery fragment
            }
        });


        return view;
    }
}
