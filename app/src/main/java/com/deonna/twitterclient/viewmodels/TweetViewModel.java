package com.deonna.twitterclient.viewmodels;

import android.content.Context;

import com.deonna.twitterclient.models.Tweet;

public class TweetViewModel {

    private Context context;
    private Tweet tweet;

    public TweetViewModel(Context context, Tweet tweet) {

        this.context = context;
        this.tweet = tweet;
    }
}
