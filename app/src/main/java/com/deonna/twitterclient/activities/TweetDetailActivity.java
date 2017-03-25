package com.deonna.twitterclient.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityTweetDetailBinding;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
    }

    private void loadProfileImage(String url) {

        int size = TweetDetailViewModel.getProfileImageSize();

        Glide.with(this)
                .load(url)
                .override(size, size)
                .bitmapTransform(new RoundedCornersTransformation(this, 10, 2))
                .into(binding.ivProfileImage);
    }
}
