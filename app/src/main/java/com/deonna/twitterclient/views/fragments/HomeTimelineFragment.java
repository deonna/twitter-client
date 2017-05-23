package com.deonna.twitterclient.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.viewmodels.HomeTimelineViewModel;

public class HomeTimelineFragment extends TweetsListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        super.setupViewModel(new HomeTimelineViewModel(getContext(), getFragmentManager(), this));
        super.setupBindings(inflater, parent);

        super.setupTimelineView();
        super.setupSwipeToRefresh();

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
}
