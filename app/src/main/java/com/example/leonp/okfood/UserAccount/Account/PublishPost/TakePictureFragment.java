package com.example.leonp.okfood.UserAccount.Account.PublishPost;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leonp.okfood.R;

public class TakePictureFragment extends Fragment {

    private static final String TAG = "TakePictureFragment";

    // widgets
    private ImageView ivCameraButton;

    // vars
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_takepicture, container, false);

        mContext = getActivity();

//        ivCameraButton = (ImageView) view.findViewById(R.id.ivCameraButton);
//
//        ivCameraButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Log.d(TAG, "onLongClick: inputted view is: " + v);
//                ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
//                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
//
//                ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
//                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(ivCameraButton);
//
//                v.startDrag(dragData,myShadow,null,0);
//                return true;
//            }
//        });
//
//        ivCameraButton.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View view, DragEvent dragEvent) {
//                int action = dragEvent.getAction();
//                Log.d(TAG, "onDrag: Action int is: " + action);
//
//                switch (action) {
//                    case DragEvent.ACTION_DRAG_STARTED:
//                        Toast.makeText(mContext, "Started Drag", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onDrag: Started dragging");
//                        return true;
//                    case DragEvent.ACTION_DRAG_ENDED:
//                        Toast.makeText(mContext, "Drag Ended", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onDrag: Ended Dragging");
//                        return true;
//                    default:
//                        return true;
//                }
//            }
//        });
//
        return view;
    }

}
