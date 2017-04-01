package com.deonna.twitterclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.viewmodels.FollowersListViewModel;

import org.parceler.Parcels;

public class FollowersListFragment extends UsersListFragment {

    public static FollowersListFragment newInstance(User user) {

        FollowersListFragment followersListFragment = new FollowersListFragment();

        Bundle args = new Bundle();
        args.putParcelable(UsersListFragment.KEY_USER, Parcels.wrap(user));

        followersListFragment.setArguments(args);

        return followersListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        User user = getUser();

        setupViewModel(new FollowersListViewModel(getActivity(), user));

        setupBindings(inflater, parent);
        setupUsersListView();

        return binding.getRoot();
    }
}
