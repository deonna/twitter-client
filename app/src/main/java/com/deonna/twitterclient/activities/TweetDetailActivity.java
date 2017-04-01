package com.deonna.twitterclient.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityTweetDetailBinding;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String KEY_TWEET = "tweet";

    private TweetDetailViewModel tweetDetailViewModel;
    private ActivityTweetDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(KEY_TWEET));

        tweetDetailViewModel = new TweetDetailViewModel(TweetDetailActivity.this, tweet);

        binding = DataBindingUtil.setContentView(TweetDetailActivity.this, R.layout.activity_tweet_detail);
        binding.setTweetDetailViewModel(tweetDetailViewModel);
        binding.executePendingBindings();

        loadProfileImage(tweet.user.getLargeProfileImageUrl());

        loadMedia(tweet.getMediaUrl());
    }

    private void loadProfileImage(String url) {

        int size = TweetDetailViewModel.getProfileImageSize();

        Images.loadFromUrl(this, binding.ivProfileImage, url, size, size);
    }

    private void loadMedia(String url) {

        Images.loadFromUrlWithFixedSizeRoundedTop(this, binding.ivMedia, url);
    }
}
