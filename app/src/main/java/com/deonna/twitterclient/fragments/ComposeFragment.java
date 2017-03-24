package com.deonna.twitterclient.fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.deonna.twitterclient.R;


public class ComposeFragment extends DialogFragment {

    public static final String LAYOUT_NAME = "fragment_compose";

    public static ComposeFragment newInstance() {

        ComposeFragment composeFragment = new ComposeFragment();

//        Bundle args = new Bundle();

//        composeFragment.setArguments(args);

        return composeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View fragmentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_compose, container);

        return fragmentView;
    }

    @Override
    public void onResume() {

        int height = getResources().getDimensionPixelSize(R.dimen.dialog_compose_height);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);

        super.onResume();
    }
}
