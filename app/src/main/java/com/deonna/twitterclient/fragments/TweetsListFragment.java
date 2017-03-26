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
import com.deonna.twitterclient.databinding.FragmentTweetsListBinding;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.viewmodels.TweetsListViewModel;

public class TweetsListFragment extends Fragment {

    protected TweetsListViewModel tweetsListViewModel;
    protected FragmentTweetsListBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        setupViewModel(new TweetsListViewModel(getContext(), getFragmentManager()));
        setupBindings(inflater, parent);

        setupTimelineView();

        return binding.getRoot();
    }

    public void setupViewModel(TweetsListViewModel viewModel) {

        tweetsListViewModel = viewModel;
        tweetsListViewModel.onCreate();
    }

    public void setupBindings(LayoutInflater inflater, @Nullable ViewGroup parent) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweets_list, parent, false);

        binding.setTweetsListViewModel(tweetsListViewModel);
        binding.executePendingBindings();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public void scrollToTop() {

        binding.rvTimeline.scrollToPosition(0);
    }

    protected void setupTimelineView() {

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.rvTimeline.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        binding.rvTimeline.setLayoutManager(layoutManager);
        binding.rvTimeline.setAdapter(tweetsListViewModel.getAdapter());

        EndlessRecyclerViewScrollListener scrollListener = tweetsListViewModel.initializeEndlessScrollListener(layoutManager);

        binding.rvTimeline.addOnScrollListener(scrollListener);
    }
}
