package com.deonna.twitterclient.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityProfileBinding;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.viewmodels.ProfileViewModel;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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

    }

    private void loadImage(String url, ImageView ivImage) {

        int size = TweetDetailViewModel.getProfileImageSize();

        Glide.with(this)
                .load(url)
                .override(size, size)
                .bitmapTransform(new RoundedCornersTransformation(this, 10, 2))
                .into(ivImage);
    }
}
