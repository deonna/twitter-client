package com.deonna.twitterclient.views.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.events.callbacks.NewTweetsListener;
import com.deonna.twitterclient.databinding.ActivityTimelineBinding;
import com.deonna.twitterclient.views.fragments.ComposeFragment;
import com.deonna.twitterclient.views.fragments.HomeTimelineFragment;
import com.deonna.twitterclient.views.fragments.MentionsTimelineFragment;
import com.deonna.twitterclient.views.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.deonna.twitterclient.viewmodels.TimelineViewModel;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimelineActivity extends AppCompatActivity implements NewTweetsListener {

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

        setupDrawerContent();
        setupTabs();
    }

    @Override
    public void displayNewestTweets() {

        TweetsListFragment tweetsListFragment = getCurrentFragment();

        tweetsListFragment.addNewlyComposedTweet();
        tweetsListFragment.scrollToTop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);

        setupSearchView(menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                binding.dlMain.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupNavHeader(User user) {

        View headerLayout = binding.nvMain.inflateHeaderView(R.layout.nav_header);

        ImageView ivProfileImage = (ImageView) headerLayout.findViewById(R.id.ivProfileImage);
        ImageView ivBannerImage = (ImageView) headerLayout.findViewById(R.id.ivBannerImage);

        TextView tvName = (TextView) headerLayout.findViewById(R.id.tvName);
        TextView tvScreenName = (TextView) headerLayout.findViewById(R.id.tvScreenName);

        tvName.setText(user.name);
        tvScreenName.setText("@" + user.screenName);

        Images.loadCircularImage(TimelineActivity.this, ivProfileImage, user.getLargeProfileImageUrl());
        Images.loadFromUrlWithFixedSizeRegularCorners(TimelineActivity.this, ivBannerImage, user.getBannerImageUrl());

        ivProfileImage.setOnClickListener((view) -> {
            openCurrentUserProfile();
        });
    }

    private void setupDrawerContent() {

        binding.nvMain.setNavigationItemSelectedListener((menuItem) -> {

            selectDrawerItem(menuItem);
            return true;
        });
    }

    private void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;

        Class fragmentClass;

        switch(menuItem.getItemId()) {

            case R.id.nav_direct_messages:
                Intent intent = new Intent(TimelineActivity.this, DirectMessagesActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_trends:
                break;
            case R.id.nav_logout:
                TwitterApplication.getRestClient().logOut();
                getLoginScreen();
                break;
        }

        menuItem.setChecked(true);

        binding.dlMain.closeDrawers();
    }

    private void getLoginScreen() {

        finish();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void setupSearchView(Menu menu) {

        MenuItem searchItem = menu.findItem(R.id.miSearch);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(TimelineActivity.this, SearchResultsActivity.class);
                intent.putExtra(SearchResultsActivity.KEY_SEARCH_TERM, query);

                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

//    @OnClick(R.id.ivProfileImage)
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