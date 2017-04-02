package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.deonna.twitterclient.callbacks.TweetsReceivedCallback;
import com.deonna.twitterclient.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

public class UserTimelineViewModel extends TweetsTimelineViewModel {

    private String screenName;

    public UserTimelineViewModel(Context context, FragmentManager fragmentManager, TweetsListFragment fragment) {

        super(context, fragmentManager, fragment);
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void getUserTimeline() {

        client.getUserTimeline(screenName, new TweetsReceivedCallback() {

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

        client.getNextOldestUserTimelineTweets(screenName, maxId, new TweetsReceivedCallback() {

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
