package com.example.leonp.okfood.UserAccount.Account.PublishPost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leonp.okfood.R;

public class AddInstructionsFragment extends Fragment {

    private static final String TAG = "AddInstructionsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addinstructions, container, false);

//        ((PublishPostActivity) getActivity()).setCounter("3");

        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        ((PublishPostActivity) getActivity()).setCounter("3");
//    }
}
