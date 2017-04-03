package com.deonna.twitterclient.activities;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityProfileBinding;
import com.deonna.twitterclient.fragments.DirectMessagesListFragment;
import com.deonna.twitterclient.fragments.DirectMessagesReceivedFragment;
import com.deonna.twitterclient.fragments.DirectMessagesSentFragment;
import com.deonna.twitterclient.fragments.FavoritesTimelineFragment;
import com.deonna.twitterclient.fragments.FollowersListFragment;
import com.deonna.twitterclient.fragments.FollowingListFragment;
import com.deonna.twitterclient.fragments.TweetsListFragment;
import com.deonna.twitterclient.fragments.UserTimelineFragment;
import com.deonna.twitterclient.fragments.UsersListFragment;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.ProfileViewModel;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class ProfileActivity extends AppCompatActivity {

    public static final String KEY_USER = "user";

    private ProfileViewModel profileViewModel;
    private ActivityProfileBinding binding;

    private User user;

    private ProfilePagerAdapter profilePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(KEY_USER));

        profileViewModel = new ProfileViewModel(ProfileActivity.this, user);
        profileViewModel.onCreate();

        binding = DataBindingUtil.setContentView(ProfileActivity.this, R.layout.activity_profile);

        binding.setProfileViewModel(profileViewModel);

        setupToolbar();

        loadImage(profileViewModel.getLargeProfileImageUrl(), binding.ivProfileImage);
        loadBackgroundImage(profileViewModel.getBannerImageUrl(), binding.ivBannerImage);

        setupFollowersClickListener(user);
        setupFollowingClickListener();

        setupTabs();
    }

    private void setupToolbar() {

        setSupportActionBar(binding.tbMain);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setupFollowersClickListener(User user) {

        binding.tvFollowers.setOnTouchListener((view, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FollowersListFragment followersListFragment = FollowersListFragment.newInstance(user);
                followersListFragment.show(fragmentManager, FollowersListFragment.LAYOUT_NAME);
            }

            return true;
        });
    }

    private void setupFollowingClickListener() {

        binding.tvFollowing.setOnTouchListener((view, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FollowingListFragment followingListFragment = FollowingListFragment.newInstance(user);
                followingListFragment.show(fragmentManager, FollowingListFragment.LAYOUT_NAME);
            }

            return true;
        });
    }

    private void loadImage(String url, ImageView ivImage) {

        int size = TweetDetailViewModel.getProfileImageSize();

        Images.loadFromUrl(this, ivImage, url, size, size);
    }

    private void loadBackgroundImage(String url, ImageView ivImage) {

        Images.loadFromUrlWithFixedSizeRegularCorners(this, ivImage, url);
    }

    private void setupTabs() {

        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());

        binding.vpTimelines.setAdapter(profilePagerAdapter);
        binding.pstsTimelines.setViewPager(binding.vpTimelines);
    }

    public class ProfilePagerAdapter extends FragmentPagerAdapter {

        final int TWEETS_POSITION = 0;
        final int LIKES_POSITION = 1;

        final String[] tabTitles = { "Tweets", "Likes" };

        Map<Integer, TweetsListFragment> positionToFragment;

        public ProfilePagerAdapter(FragmentManager fm) {

            super(fm);

            positionToFragment = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case TWEETS_POSITION:
                    UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
                    positionToFragment.put(position, userTimelineFragment);

                    return userTimelineFragment;

                case LIKES_POSITION:
                    FavoritesTimelineFragment favoritesTimelineFragment = new FavoritesTimelineFragment();
                    positionToFragment.put(position, favoritesTimelineFragment);

                    return favoritesTimelineFragment;

                default:
                    UserTimelineFragment defaultFragment = new UserTimelineFragment();
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
