package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.deonna.twitterclient.callbacks.TweetsReceivedCallback;
import com.deonna.twitterclient.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

/**
 * Created by deonna on 4/2/17.
 */

public class FavoritesTimelineViewModel extends TweetsTimelineViewModel {

    private String screenName;

    public FavoritesTimelineViewModel(Context context, FragmentManager fragmentManager, TweetsListFragment fragment) {

        super(context, fragmentManager, fragment);
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void getFavoritesTimeline() {

        client.getFavoritesTimeline(screenName, new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsReceivedError() {

            }
        });
    }

    @Override
    protected void getNextOldestTweets() {

        client.getNextOldestFavoritesTimelineTweets(screenName, maxId, new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsReceivedError() {

            }
        });
    }
}