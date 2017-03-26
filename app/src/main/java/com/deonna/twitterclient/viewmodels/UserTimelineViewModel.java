package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

public class UserTimelineViewModel extends TweetsListViewModel {

    private String screenName;

    public UserTimelineViewModel(Context context, FragmentManager fragmentManager) {

        super(context, fragmentManager);
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void getUserTimeline() {

        client.getUserTimeline(screenName, new TweetsCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {
                Log.d(TAG, newTweets.toString());
                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsError() {

            }
        });
    }

    @Override
    protected void getNextOldestTweets() {

        client.getNextOldestUserTimelineTweets(screenName, maxId, new TweetsCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsError() {

            }
        });
    }
}
