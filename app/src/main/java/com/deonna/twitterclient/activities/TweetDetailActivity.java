package com.deonna.twitterclient.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.callbacks.NewTweetsListener;
import com.deonna.twitterclient.databinding.ActivityTweetDetailBinding;
import com.deonna.twitterclient.databinding.ItemTweetBinding;
import com.deonna.twitterclient.fragments.ReplyFragment;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;
import com.deonna.twitterclient.viewmodels.TweetListViewModel;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity implements NewTweetsListener {

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

        setupRepliesClickListener(tweet);
        setupProfileImageClickListener(tweet);
        setupRetweetsClickListener(tweet);
        setupFavoritesClickListener(tweet);
    }


    @Override
    public void displayNewestTweets() {

    }

    private void loadProfileImage(String url) {

        int size = TweetDetailViewModel.getProfileImageSize();

        Images.loadFromUrl(this, binding.ivProfileImage, url, size, size);
    }

    private void loadMedia(String url) {

        Images.loadFromUrlWithFixedSizeRoundedTop(this, binding.ivMedia, url);
    }

    private void setupRepliesClickListener(Tweet tweet) {

        binding.ivReplies.setOnClickListener((view) -> {

            ReplyFragment replyFragment = ReplyFragment.newInstance(tweet.user);
            replyFragment.show(getSupportFragmentManager(), ReplyFragment.LAYOUT_NAME);
        });
    }

    private void setupProfileImageClickListener(Tweet tweet) {

        binding.ivProfileImage.setOnClickListener((view) -> {

            Intent intent = new Intent(TweetDetailActivity.this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.KEY_USER, Parcels.wrap(tweet.user));

            startActivity(intent);
        });
    }

    private void setupRetweetsClickListener(Tweet tweet) {

        binding.ivRetweets.setOnClickListener((view) -> {

            tweetDetailViewModel.retweet(
                    tweet.id,
                    binding.ivRetweets
            );
        });

    }

    private void setupFavoritesClickListener(Tweet tweet) {

        binding.ivFavorites.setOnClickListener((view) -> {

            if (tweet.favorited) {
                tweetDetailViewModel.unfavorite(
                        tweet.id,
                        binding.ivFavorites
                );
            } else {
                tweetDetailViewModel.favorite(
                        tweet.id,
                        binding.ivFavorites
                );
            }
        });
    }
}
