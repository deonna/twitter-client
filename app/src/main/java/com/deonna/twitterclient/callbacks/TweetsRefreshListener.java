package com.deonna.twitterclient.callbacks;

/**
 * Created by deonna on 3/24/17.
 */

public interface TweetsRefreshListener {

    void getNewestTweets();
    void finishRefreshing();
}
