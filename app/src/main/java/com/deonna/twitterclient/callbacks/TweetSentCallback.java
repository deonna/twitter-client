package com.deonna.twitterclient.callbacks;

public interface TweetSentCallback {

    void onTweetSent(String newTweet);
    void onTweetSentFailed();
}
