package com.deonna.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.activities.ProfileActivity;
import com.deonna.twitterclient.activities.TweetDetailActivity;
import com.deonna.twitterclient.databinding.ItemTweetBinding;
import com.deonna.twitterclient.fragments.ReplyFragment;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.TweetListViewModel;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Tweet> tweets;

    private Context context;
    private FragmentManager fragmentManager;

    public TweetsAdapter(Context context, List<Tweet> tweets, FragmentManager fragmentManger) {

        this.context = context;
        this.tweets = tweets;
        this.fragmentManager = fragmentManger;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemTweetBinding tweetBinding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_tweet,
                        parent,
                        false
                );

        return new TweetViewHolder(tweetBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Tweet tweet = tweets.get(position);

        TweetViewHolder tweetHolder = (TweetViewHolder) holder;
        ItemTweetBinding tweetBinding = tweetHolder.binding;
        TweetListViewModel tweetListViewModel = new TweetListViewModel(context, tweet, this);

        tweetBinding.setTweetListViewModel(tweetListViewModel);
        tweetBinding.executePendingBindings();

        setupTweetDetailClickListener(tweetBinding, tweet);
        setupRepliesClickListener(tweetBinding, tweet);
        setupProfileImageClickListener(tweetBinding, tweet);

        setupRetweetsClickListener(tweetBinding, tweetListViewModel, tweet, position);
        setupFavoritesClickListener(tweetBinding, tweetListViewModel, tweet, position);

        loadProfileImage(
                tweet.user.getLargeProfileImageUrl(),
                tweetBinding.ivProfileImage,
                TweetListViewModel.getProfileImageSize()
        );

        loadMedia(tweet.getMediaUrl(), tweetBinding.ivMedia);
    }

    @Override
    public int getItemCount() {

        return tweets.size();
    }

    private void setupTweetDetailClickListener(ItemTweetBinding tweetBinding, Tweet tweet) {

        tweetBinding.rlTweetLayout.setOnClickListener((view) -> {

            Intent intent = new Intent(context, TweetDetailActivity.class);
            intent.putExtra(TweetDetailActivity.KEY_TWEET, Parcels.wrap(tweet));

            context.startActivity(intent);
        });
    }

    private void setupRepliesClickListener(ItemTweetBinding tweetBinding, Tweet tweet) {

        tweetBinding.ivReplies.setOnClickListener((view) -> {

            ReplyFragment replyFragment = ReplyFragment.newInstance(tweet.user);
            replyFragment.show(fragmentManager, ReplyFragment.LAYOUT_NAME);
        });
    }

    private void setupProfileImageClickListener(ItemTweetBinding tweetBinding, Tweet tweet) {

        tweetBinding.ivProfileImage.setOnClickListener((view) -> {

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra(ProfileActivity.KEY_USER, Parcels.wrap(tweet.user));

            context.startActivity(intent);
        });
    }

    private void setupRetweetsClickListener(ItemTweetBinding tweetBinding, TweetListViewModel tweetListViewModel, Tweet tweet, int position) {

        tweetBinding.ivRetweets.setOnClickListener((view) -> {

            tweetListViewModel.retweet(tweet.id, position, tweetBinding.ivRetweets, tweetBinding
                    .tvRetweets);
        });

    }

    private void setupFavoritesClickListener(ItemTweetBinding tweetBinding, TweetListViewModel tweetListViewModel, Tweet tweet, int position) {

        tweetBinding.ivFavorites.setOnClickListener((view) -> {

            if (tweet.favorited) {
                tweetListViewModel.unfavorite(tweet.id, position, tweetBinding.ivFavorites, tweetBinding
                        .tvFavorites);
            } else {
                tweetListViewModel.favorite(tweet.id, position, tweetBinding.ivFavorites, tweetBinding
                        .tvFavorites);
            }
        });
    }

    private void loadProfileImage(String url, ImageView ivProfileImage, int size) {

        Images.loadFromUrl(context, ivProfileImage, url, size, size);
    }

    private void loadMedia(String url, ImageView ivImage) {

        Images.loadFromUrlWithFixedSize(context, ivImage, url);

//        Glide.with(context)
//                .load(url)
//                .bitmapTransform(new RoundedCornersTransformation(context, 10, 2))
//                .into(ivImage);
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {

        final ItemTweetBinding binding;

        public TweetViewHolder(ItemTweetBinding itemView) {

            super(itemView.getRoot());

            binding = itemView;
        }
    }
}
