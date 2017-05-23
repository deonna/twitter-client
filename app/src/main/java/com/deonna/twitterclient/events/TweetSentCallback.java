package com.deonna.twitterclient.events.callbacks;

public interface TweetSentCallback {

    void onTweetSent(String newTweet);
    void onTweetSentFailed();
}
