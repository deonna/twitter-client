package com.deonna.twitterclient.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Toast;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.callbacks.TweetsRefreshListener;
import com.deonna.twitterclient.databinding.ActivityTimelineBinding;
import com.deonna.twitterclient.fragments.ComposeFragment;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.TimelineViewModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimelineActivity extends AppCompatActivity implements TweetsRefreshListener {

    private TimelineViewModel timelineViewModel;
    private ActivityTimelineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timelineViewModel = new TimelineViewModel(TimelineActivity.this);

        binding = DataBindingUtil.setContentView(TimelineActivity.this, R.layout.activity_timeline);
        binding.setTimelineViewModel(timelineViewModel);

        setupTimelineView();
        setupToolbar();
        setupSwipeToRefresh();

        timelineViewModel.onCreate();

        Fonts.setupFonts(getAssets()); //TODO: Move to SplashActivity when created

        ButterKnife.bind(this);
    }

    private void setupTimelineView() {

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.rvTimeline.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(TimelineActivity.this);

        binding.rvTimeline.setLayoutManager(layoutManager);
        binding.rvTimeline.setAdapter(timelineViewModel.getAdapter());

        EndlessRecyclerViewScrollListener scrollListener = timelineViewModel.initializeEndlessScrollListener(layoutManager);

        binding.rvTimeline.addOnScrollListener(scrollListener);
    }

    private void setupToolbar() {

        setSupportActionBar(binding.tbMain);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupSwipeToRefresh() {

        binding.srlTimeline.setOnRefreshListener(() -> {

            timelineViewModel.getNewestTweets();
        });
    }

    @OnClick(R.id.fabCompose)
    public void openNewTweetDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(timelineViewModel.getCurrentUser());
        composeFragment.show(fragmentManager, ComposeFragment.LAYOUT_NAME);
    }

    @OnClick(R.id.ivLogo)
    public void scrollToTop() {

        binding.rvTimeline.scrollToPosition(0);
    }

    @Override
    public void getNewestTweets() {

        timelineViewModel.getNewestTweets();
    }

    @Override
    public void finishRefreshing() {

        binding.srlTimeline.setRefreshing(false);
    }
}