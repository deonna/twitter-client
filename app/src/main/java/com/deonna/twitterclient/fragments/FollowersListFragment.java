package com.deonna.twitterclient.fragments;

import android.content.Context;

import com.deonna.twitterclient.viewmodels.FollowersListViewModel;

/**
 * Created by deonna on 4/1/17.
 */

public class FollowersListFragment extends UsersListFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        usersListViewModel = new FollowersListViewModel(context);
    }
}
