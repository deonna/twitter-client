package com.deonna.twitterclient.activities;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityProfileBinding;
import com.deonna.twitterclient.fragments.FollowersListFragment;
import com.deonna.twitterclient.fragments.FollowingListFragment;
import com.deonna.twitterclient.fragments.UsersListFragment;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.ProfileViewModel;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class ProfileActivity extends AppCompatActivity {

    public static final String KEY_USER = "user";

    private ProfileViewModel profileViewModel;
    private ActivityProfileBinding binding;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(KEY_USER));

        profileViewModel = new ProfileViewModel(ProfileActivity.this, user);
        profileViewModel.onCreate();

        binding = DataBindingUtil.setContentView(ProfileActivity.this, R.layout.activity_profile);

        binding.setProfileViewModel(profileViewModel);

        loadImage(profileViewModel.getLargeProfileImageUrl(), binding.ivProfileImage);
        loadBackgroundImage(profileViewModel.getBannerImageUrl(), binding.ivBannerImage);

        setupFollowersClickListener(user);
        setupFollowingClickListener();
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

        Images.loadFromUrlWithFixedSize(this, ivImage, url);
    }
}
