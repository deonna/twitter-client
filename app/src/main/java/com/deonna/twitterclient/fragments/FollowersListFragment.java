package com.deonna.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.FollowersListViewModel;

import org.parceler.Parcels;

public class FollowersListFragment extends UsersListFragment {

    private static final String TITLE = "Followers";

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

        addStylingToTitle();

        User user = getUser();

        setupViewModel(new FollowersListViewModel(getActivity(), user));

        setupBindings(inflater, parent);
        setupUsersListView();

        return binding.getRoot();
    }

    private void addStylingToTitle() {

        TextView tvTitle = (TextView) getDialog().findViewById(android.R.id.title);
        tvTitle.setTypeface(Fonts.fontBold);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setText(TITLE);
        tvTitle.setPadding(0, 20, 0, 20);
    }
}
