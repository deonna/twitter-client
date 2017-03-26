package com.deonna.twitterclient.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.activities.TimelineActivity;
import com.deonna.twitterclient.databinding.FragmentTweetsListBinding;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.viewmodels.TweetsListViewModel;

public class TweetsListFragment extends Fragment {

    private TweetsListViewModel tweetsListViewModel;
    private FragmentTweetsListBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        tweetsListViewModel = new TweetsListViewModel(getContext(), getFragmentManager());

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweets_list, parent, false);

        View fragmentView = binding.getRoot();

        binding.setTweetsListViewModel(tweetsListViewModel);
        binding.executePendingBindings();

        setupTimelineView();

        tweetsListViewModel.onCreate();

        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void scrollToTop() {

        binding.rvTimeline.scrollToPosition(0);
    }

    private void setupTimelineView() {

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.rvTimeline.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        binding.rvTimeline.setLayoutManager(layoutManager);
        binding.rvTimeline.setAdapter(tweetsListViewModel.getAdapter());

        EndlessRecyclerViewScrollListener scrollListener = tweetsListViewModel.initializeEndlessScrollListener(layoutManager);

        binding.rvTimeline.addOnScrollListener(scrollListener);
    }
}
