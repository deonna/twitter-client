package com.deonna.twitterclient.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.viewmodels.DirectMessagesSentViewModel;

/**
 * Created by deonna on 4/2/17.
 */

public class DirectMessagesSentFragment extends DirectMessagesListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        setupViewModel(new DirectMessagesSentViewModel(getContext(), getFragmentManager()));
        setupBindings(inflater, parent);

        setupMessagesView();

        return binding.getRoot();
    }
}
