package com.deonna.twitterclient.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ItemTweetBinding;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.TweetViewModel;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tweet> tweets;
    private Context context;

    public TweetsAdapter(Context context, List<Tweet> tweets) {

        this.context = context;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Tweet tweet = tweets.get(position);

        TweetViewHolder tweetHolder = (TweetViewHolder) holder;
        ItemTweetBinding tweetBinding = tweetHolder.binding;
        TweetViewModel tweetViewModel = new TweetViewModel(context, tweet);

        tweetBinding.setTweetViewModel(tweetViewModel);
        tweetBinding.executePendingBindings();

        loadProfileImage(
                tweet.user.getLargeProfileImageUrl(),
                tweetBinding.ivProfileImage,
                TweetViewModel.getProfileImageSize()
        );
    }

    @Override
    public int getItemCount() {

        return tweets.size();
    }

    private void loadProfileImage(String url, ImageView ivProfileImage, int size) {

        Glide.with(context)
                .load(url)
                .override(size, size)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 2))
                .into(ivProfileImage);
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {

        final ItemTweetBinding binding;

        public TweetViewHolder(ItemTweetBinding itemView) {

            super(itemView.getRoot());

            binding = itemView;
            setTypefaces();
        }

        private void setTypefaces() {

            binding.tvFavorites.setTypeface(Fonts.fontRegular);
            binding.tvReplies.setTypeface(Fonts.fontRegular);
            binding.tvRetweets.setTypeface(Fonts.fontRegular);

            binding.tvScreenName.setTypeface(Fonts.fontRegular);
            binding.tvTimestamp.setTypeface(Fonts.fontRegular);
            binding.tvName.setTypeface(Fonts.fontBold);
            binding.tvTweet.setTypeface(Fonts.fontRegular);
        }
    }
}
