package com.deonna.twitterclient.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityTimelineBinding;
import com.deonna.twitterclient.fragments.ComposeFragment;
import com.deonna.twitterclient.fragments.HomeTimelineFragment;
import com.deonna.twitterclient.fragments.MentionsTimelineFragment;
import com.deonna.twitterclient.fragments.TweetsListFragment;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.TimelineViewModel;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TimelineActivity extends AppCompatActivity {

    private TimelineViewModel timelineViewModel;
    private ActivityTimelineBinding binding;

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

        ViewPager vpTimelines = (ViewPager) findViewById(R.id.vpTimelines);
        vpTimelines.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip pstsTimelines = (PagerSlidingTabStrip) findViewById(R.id.pstsTimelines);
        pstsTimelines.setViewPager(vpTimelines);
    }

    private void setupToolbar() {

        setSupportActionBar(binding.tbMain);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.fabCompose)
    public void openNewTweetDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(timelineViewModel.getCurrentUser());
        composeFragment.show(fragmentManager, ComposeFragment.LAYOUT_NAME);
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

        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.KEY_USER, Parcels.wrap(timelineViewModel.getCurrentUser()));

        startActivity(intent);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {

        final int HOME_POSITION = 0;
        final int MENTIONS_POSITION = 1;

        final String[] tabTitles = { "Home", "Mentions" };

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case HOME_POSITION:
                    return new HomeTimelineFragment();

                case MENTIONS_POSITION:
                    return new MentionsTimelineFragment();

                default:
                    return new HomeTimelineFragment();

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
    }
}