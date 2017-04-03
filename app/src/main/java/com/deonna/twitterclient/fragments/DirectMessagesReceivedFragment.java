package com.deonna.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.viewmodels.DirectMessagesReceivedViewModel;

/**
 * Created by deonna on 4/2/17.
 */

public class DirectMessagesReceivedFragment extends DirectMessagesListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        setupViewModel(new DirectMessagesReceivedViewModel(getContext(), getFragmentManager()));
        setupBindings(inflater, parent);

        setupMessagesView();

        return binding.getRoot();
    }
}
