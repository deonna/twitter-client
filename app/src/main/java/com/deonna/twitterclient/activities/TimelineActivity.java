package com.deonna.twitterclient.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityTimelineBinding;
import com.deonna.twitterclient.fragments.ComposeFragment;
import com.deonna.twitterclient.fragments.HomeTimelineFragment;
import com.deonna.twitterclient.fragments.MentionsTimelineFragment;
import com.deonna.twitterclient.fragments.TweetsListFragment;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.TimelineViewModel;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TimelineActivity extends AppCompatActivity {

    private TimelineViewModel timelineViewModel;
    private ActivityTimelineBinding binding;
    private TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timelineViewModel = new TimelineViewModel(TimelineActivity.this, getSupportFragmentManager());

        binding = DataBindingUtil.setContentView(TimelineActivity.this, R.layout.activity_timeline);
        binding.setTimelineViewModel(timelineViewModel);

        setupToolbar();

        timelineViewModel.onCreate();

        Fonts.setupFonts(getAssets()); //TODO: Move to SplashActivity/LoginActivity when created

        ButterKnife.bind(this);

        setupTabs();
    }

    private void setupToolbar() {

        setSupportActionBar(binding.tbMain);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupTabs() {

        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());

        binding.vpTimelines.setAdapter(tweetsPagerAdapter);
        binding.pstsTimelines.setViewPager(binding.vpTimelines);
    }

    @OnClick(R.id.fabCompose)
    public void openNewTweetDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(timelineViewModel.getCurrentUser());
        composeFragment.show(fragmentManager, ComposeFragment.LAYOUT_NAME);
    }

    public void loadCurrentUserProfileImage(String url) {

        int size = TweetDetailViewModel.getProfileImageSize();

        Images.loadFromUrl(this, binding.ivProfileImage, url, size, size);
    }

    @OnClick(R.id.ivProfileImage)
    public void openCurrentUserProfile() {

        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.KEY_USER, Parcels.wrap(timelineViewModel.getCurrentUser()));

        startActivity(intent);
    }

    @OnClick(R.id.ivLogo)
    public void scrollToTop() {

        getCurrentFragment().scrollToTop();
    }

    private TweetsListFragment getCurrentFragment() {

        int position = binding.vpTimelines.getCurrentItem();

        TweetsPagerAdapter tweetsPagerAdapter = (TweetsPagerAdapter) binding.vpTimelines.getAdapter();

        return tweetsPagerAdapter.getCurrentFragment(position);
    }

    public void addNewlyComposedTweet() {

        TweetsListFragment tweetsListFragment = getCurrentFragment();

        tweetsListFragment.addNewlyComposedTweet();
        tweetsListFragment.scrollToTop();
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {

        final int HOME_POSITION = 0;
        final int MENTIONS_POSITION = 1;

        final String[] tabTitles = { "Home", "Mentions" };


        Map<Integer, TweetsListFragment> positionToFragment;

        public TweetsPagerAdapter(FragmentManager fm) {

            super(fm);

            positionToFragment = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case HOME_POSITION:
                    HomeTimelineFragment homeTimelineFragment =  new HomeTimelineFragment();
                    positionToFragment.put(position, homeTimelineFragment);

                    return homeTimelineFragment;

                case MENTIONS_POSITION:
                    MentionsTimelineFragment mentionsTimelineFragment = new MentionsTimelineFragment();
                    positionToFragment.put(position, mentionsTimelineFragment);

                    return mentionsTimelineFragment;

                default: //TODO: reduce duplication
                    HomeTimelineFragment defaultFragment =  new HomeTimelineFragment();
                    positionToFragment.put(position, defaultFragment);

                    return defaultFragment;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public TweetsListFragment getCurrentFragment(int position) {

            return positionToFragment.get(position);
        }
    }
}