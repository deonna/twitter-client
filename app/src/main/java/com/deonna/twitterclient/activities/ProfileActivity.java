package com.deonna.twitterclient.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityProfileBinding;
import com.deonna.twitterclient.fragments.UsersListFragment;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.ProfileViewModel;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    public static final String KEY_USER = "user";

    private ProfileViewModel profileViewModel;
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        User user = (User) Parcels.unwrap(getIntent().getParcelableExtra(KEY_USER));

        profileViewModel = new ProfileViewModel(ProfileActivity.this, user);
        profileViewModel.onCreate();

        binding = DataBindingUtil.setContentView(ProfileActivity.this, R.layout.activity_profile);

        binding.setProfileViewModel(profileViewModel);

        loadImage(profileViewModel.getLargeProfileImageUrl(), binding.ivProfileImage);
        loadBackgroundImage(profileViewModel.getBannerImageUrl(), binding.ivBannerImage);

        setupFollowersClickListener();
        setupFollowingClickListener();
    }

    private void setupFollowersClickListener() {

        binding.tvFollowers.setOnClickListener((view) -> {


        });
    }

     private void openUserListFragment() {

         FragmentManager fragmentManager = getSupportFragmentManager();
         UsersListFragment usersListFragment = UsersListFragment.newInstance();
         usersListFragment.show(fragmentManager, UsersListFragment.LAYOUT_NAME);
     }

    private void setupFollowingClickListener() {

        binding.tvFollowers.setOnClickListener((view) -> {

//            Intent intent = new Intent();
//
//            startActivity(intent);
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
