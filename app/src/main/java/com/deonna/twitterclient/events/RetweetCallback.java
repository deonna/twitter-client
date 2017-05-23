package com.deonna.twitterclient.events;

import com.deonna.twitterclient.models.Tweet;

public interface RetweetCallback {

    void onRetweet(Tweet newTweet);
    void onRetweetFailed();
}
