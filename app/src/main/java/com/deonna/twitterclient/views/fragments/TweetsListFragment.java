package com.deonna.twitterclient.views.fragments;

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
import com.deonna.twitterclient.events.ProgressBarListener;
import com.deonna.twitterclient.databinding.FragmentTweetsListBinding;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.viewmodels.TweetsTimelineViewModel;

public class TweetsListFragment extends Fragment implements ProgressBarListener {

    protected TweetsTimelineViewModel tweetsTimelineViewModel;
    protected FragmentTweetsListBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        setupViewModel(new TweetsTimelineViewModel(getContext(), getFragmentManager(), this));
        setupBindings(inflater, parent);

        setupTimelineView();
        setupSwipeToRefresh();

        return binding.getRoot();
    }

    public void setupViewModel(TweetsTimelineViewModel viewModel) {

        tweetsTimelineViewModel = viewModel;
        tweetsTimelineViewModel.onCreate();
    }

    public void setupBindings(LayoutInflater inflater, @Nullable ViewGroup parent) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweets_list, parent, false);

        binding.setTweetsTimelineViewModel(tweetsTimelineViewModel);
        binding.executePendingBindings();

    }

    protected void setupSwipeToRefresh() {

        binding.srlTimeline.setOnRefreshListener(() -> {

            tweetsTimelineViewModel.getNewestTweets(binding.srlTimeline);
        });
    }

    public void scrollToTop() {

        binding.rvTimeline.scrollToPosition(0);
    }

    public void addNewlyComposedTweet() {

        tweetsTimelineViewModel.addNewlyComposedTweet();
    }

    protected void setupTimelineView() {

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.rvTimeline.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvTimeline.setLayoutManager(layoutManager);

        binding.rvTimeline.setAdapter(tweetsTimelineViewModel.getAdapter());

        EndlessRecyclerViewScrollListener scrollListener = tweetsTimelineViewModel.initializeEndlessScrollListener(layoutManager);

        binding.rvTimeline.addOnScrollListener(scrollListener);
    }

    @Override
    public void showProgressBar() {

        binding.pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {

        binding.pbLoading.setVisibility(View.GONE);
    }
}
