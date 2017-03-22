package com.deonna.twitterclient.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ItemTweetBinding;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.viewmodels.TweetViewModel;

import java.util.ArrayList;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tweet> tweets = new ArrayList<>();
    private Context context;

    public TweetsAdapter(Context context) {

        this.context = context;
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
        tweetHolder.binding.setTweetViewModel(new TweetViewModel(context, tweet));
        tweetHolder.binding.executePendingBindings();

//        Tweet tweet = tweets.get(position);
//
//        TweetViewHolder tweetHolder = (TweetViewHolder) holder;
//        tweetHolder.binding.setTweet(tweet);
//        tweetHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {

        return tweets.size();
    }

    public List<Tweet> getTweets() {

        return tweets;
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {

        final ItemTweetBinding binding;

        public TweetViewHolder(ItemTweetBinding itemView) {

            super(itemView.getRoot());

            binding = itemView;
        }
    }
}
