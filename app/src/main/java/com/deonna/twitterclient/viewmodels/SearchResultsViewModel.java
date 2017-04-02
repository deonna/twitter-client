package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.deonna.twitterclient.callbacks.SearchResultsCallback;
import com.deonna.twitterclient.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

public class SearchResultsViewModel extends TweetsTimelineViewModel {

    private String searchTerm;

    public SearchResultsViewModel(Context context, FragmentManager fragmentManager, TweetsListFragment fragment) {

        super(context, fragmentManager, fragment);
    }

    public void setSearchTerm(String searchTerm) {

        this.searchTerm = searchTerm;
    }

    public void getSearchResults() {

        client.getSearchResults(searchTerm, null, new SearchResultsCallback() {

            @Override
            public void onSearchResultsReceived(List<Tweet> results) {

                tweets.addAll(results);
                tweetsAdapter.notifyDataSetChanged();

                if (!results.isEmpty()) {
                    maxId = getMaxIdForNextFetch(results);
                }
            }

            @Override
            public void onSearchResultsError() {

            }
        });
    }

    @Override
    protected void getNextOldestTweets() {

        client.getSearchResults(searchTerm, maxId, new SearchResultsCallback() {

            @Override
            public void onSearchResultsReceived(List<Tweet> results) {

                tweets.addAll(results);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(results);
            }

            @Override
            public void onSearchResultsError() {

            }
        });
    }
}
