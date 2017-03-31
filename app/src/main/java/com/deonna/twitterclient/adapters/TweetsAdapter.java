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
import com.deonna.twitterclient.viewmodels.TweetViewModel;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tweet> tweets;
    private Context context;
    private FragmentManager fragmentManager;

    public TweetsAdapter(Context context, List<Tweet> tweets, FragmentManager fragmentManger) {

        this.context = context;
        this.tweets = tweets;
        this.fragmentManager = fragmentManger;
    }

    public TweetsAdapter(ProfileActivity profileContext, List<Tweet> tweets) {

        this.tweets = tweets;
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
        TweetViewModel tweetViewModel = new TweetViewModel(context, tweet);

        tweetBinding.setTweetViewModel(tweetViewModel);
        tweetBinding.executePendingBindings();

        tweetBinding.rlTweetLayout.setOnClickListener((view) -> {

            Intent intent = new Intent(context, TweetDetailActivity.class);
            intent.putExtra(TweetDetailActivity.KEY_TWEET, Parcels.wrap(tweet));

            context.startActivity(intent);
        });

        tweetBinding.ivReplies.setOnClickListener((view) -> {

            ReplyFragment replyFragment = ReplyFragment.newInstance(tweet.user);
            replyFragment.show(fragmentManager, ReplyFragment.LAYOUT_NAME);
        });

        tweetBinding.ivProfileImage.setOnClickListener((view) -> {

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra(ProfileActivity.KEY_USER, Parcels.wrap(tweet.user));

            context.startActivity(intent);
        });

        tweetBinding.ivRetweets.setOnClickListener((view) -> {

            tweetViewModel.retweet(tweet.id, tweetBinding.ivRetweets, tweetBinding.tvRetweets);

//            notifyItemChanged(position);
        });

        tweetBinding.ivFavorites.setOnClickListener((view) -> {

            if (tweet.favorited) {
                tweetViewModel.unfavorite(tweet.id, tweetBinding.ivFavorites, tweetBinding.tvFavorites);
            } else {
                tweetViewModel.favorite(tweet.id, tweetBinding.ivFavorites, tweetBinding.tvFavorites);
            }

            notifyItemChanged(position);
        });

        loadProfileImage(
                tweet.user.getLargeProfileImageUrl(),
                tweetBinding.ivProfileImage,
                TweetViewModel.getProfileImageSize()
        );

        loadMedia(tweet.getMediaUrl(), tweetBinding.ivMedia);
    }

    @Override
    public int getItemCount() {

        return tweets.size();
    }

    // TODO: Make this a utlitiy function
    private void loadProfileImage(String url, ImageView ivProfileImage, int size) {

        Glide.with(context)
                .load(url)
                .override(size, size)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 2))
                .into(ivProfileImage);
    }

    private void loadMedia(String url, ImageView ivImage) {

        Glide.with(context)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 2))
                .into(ivImage);
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {

        final ItemTweetBinding binding;

        public TweetViewHolder(ItemTweetBinding itemView) {

            super(itemView.getRoot());

            binding = itemView;
        }
    }
}
