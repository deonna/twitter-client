package com.deonna.twitterclient.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.views.activities.ProfileActivity;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.viewmodels.UserTimelineViewModel;

import org.parceler.Parcels;

public class UserTimelineFragment extends TweetsListFragment {

    private UserTimelineViewModel userTimelineViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        userTimelineViewModel = new UserTimelineViewModel(getContext(), getFragmentManager(), this);

        super.setupViewModel(userTimelineViewModel);
        super.setupBindings(inflater, parent);
        super.setupTimelineView();

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        User user = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ProfileActivity.KEY_USER));
        userTimelineViewModel.setScreenName(user.screenName);

        userTimelineViewModel.getUserTimeline();
    }

}
