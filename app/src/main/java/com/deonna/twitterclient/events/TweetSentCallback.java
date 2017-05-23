package com.deonna.twitterclient.events;

public interface TweetSentCallback {

    void onTweetSent(String newTweet);
    void onTweetSentFailed();
}
