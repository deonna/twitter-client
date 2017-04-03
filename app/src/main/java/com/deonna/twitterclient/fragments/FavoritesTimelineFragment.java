package com.deonna.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.activities.ProfileActivity;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.viewmodels.FavoritesTimelineViewModel;
import com.deonna.twitterclient.viewmodels.UserTimelineViewModel;

import org.parceler.Parcels;

/**
 * Created by deonna on 4/2/17.
 */

public class FavoritesTimelineFragment extends TweetsListFragment {

    private FavoritesTimelineViewModel favoritesTimelineViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        favoritesTimelineViewModel = new FavoritesTimelineViewModel(getContext(), getFragmentManager(), this);

        super.setupViewModel(favoritesTimelineViewModel);
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

        User user = (User) Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ProfileActivity.KEY_USER));
        favoritesTimelineViewModel.setScreenName(user.screenName);

        favoritesTimelineViewModel.getFavoritesTimeline();
    }
}
