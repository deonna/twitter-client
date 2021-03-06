package com.deonna.twitterclient.views.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.FollowingListViewModel;

import org.parceler.Parcels;

public class FollowingListFragment extends UsersListFragment {

    private static final String TITLE = "Following";

    public static FollowingListFragment newInstance(User user) {

        FollowingListFragment followingListFragment = new FollowingListFragment();

        Bundle args = new Bundle();
        args.putParcelable(UsersListFragment.KEY_USER, Parcels.wrap(user));

        followingListFragment.setArguments(args);

        return followingListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        User user = getUser();

        setupViewModel(new FollowingListViewModel(getActivity(), user));

        setupBindings(inflater, parent);

        addStylingToTitle(TITLE);
        setupUsersListView();

        return binding.getRoot();
    }
}
