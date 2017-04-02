package com.deonna.twitterclient.callbacks;

import com.deonna.twitterclient.models.Tweet;

import java.util.List;

public interface SearchResultsCallback {

    void onSearchResultsReceived(List<Tweet> results);
    void onSearchResultsError();
}
