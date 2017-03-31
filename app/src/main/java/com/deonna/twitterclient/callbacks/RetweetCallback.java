package com.deonna.twitterclient.callbacks;


import com.deonna.twitterclient.models.Tweet;

public interface RetweetCallback {

    void onRetweet(Tweet newTweet);
    void onRetweetFailed();
}
