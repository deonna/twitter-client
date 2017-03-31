package com.deonna.twitterclient.callbacks;

import com.deonna.twitterclient.models.Tweet;

import java.util.List;

public interface TweetsReceivedCallback {

    void onTweetsReceived(List<Tweet> newTweets);
    void onTweetsReceivedError();
}
