package com.deonna.twitterclient.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.callbacks.TweetsRefreshListener;
import com.deonna.twitterclient.databinding.ActivityTimelineBinding;
import com.deonna.twitterclient.fragments.ComposeFragment;
import com.deonna.twitterclient.fragments.TweetsListFragment;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.TimelineViewModel;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TimelineActivity extends AppCompatActivity implements TweetsRefreshListener {

    private TimelineViewModel timelineViewModel;
    private ActivityTimelineBinding binding;
    private TweetsListFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timelineViewModel = new TimelineViewModel(TimelineActivity.this, getSupportFragmentManager());

        binding = DataBindingUtil.setContentView(TimelineActivity.this, R.layout.activity_timeline);
        binding.setTimelineViewModel(timelineViewModel);

        setupToolbar();
        setupSwipeToRefresh();

        timelineViewModel.onCreate();

        Fonts.setupFonts(getAssets()); //TODO: Move to SplashActivity/LoginActivity when created

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_tweets_list); //TODO: can I use Butterknife?
        }
    }

    @Override
    public void getNewestTweets() {

        timelineViewModel.getNewestTweets();
    }

    @Override
    public void finishRefreshing() {

        binding.srlTimeline.setRefreshing(false);
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

        fragmentTweetsList.scrollToTop();
    }

    public void loadCurrentUserProfileImage(String url) {

        int size = TweetDetailViewModel.getProfileImageSize();

        Glide.with(this)
                .load(url)
                .override(size, size)
                .bitmapTransform(new RoundedCornersTransformation(this, 10, 2))
                .into(binding.ivProfileImage);
    }

    @OnClick(R.id.ivProfileImage)
    public void openCurrentUserProfile() {

        binding.ivProfileImage.setOnClickListener((view) -> {

            Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.KEY_USER, Parcels.wrap(timelineViewModel.getCurrentUser()));

            startActivity(intent);
        });
    }
}