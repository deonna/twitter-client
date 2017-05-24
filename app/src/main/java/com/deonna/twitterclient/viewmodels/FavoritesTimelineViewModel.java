package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.deonna.twitterclient.events.TweetsReceivedCallback;
import com.deonna.twitterclient.network.requests.TimelineRequest;
import com.deonna.twitterclient.views.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

import static com.deonna.twitterclient.network.requests.TimelineRequest.FAVORITES_TIMELINE_PATH;

public class FavoritesTimelineViewModel extends TweetsTimelineViewModel {

    private String screenName;

    public FavoritesTimelineViewModel(Context context, FragmentManager fragmentManager, TweetsListFragment fragment) {

        super(context, fragmentManager, fragment);
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void getFavoritesTimeline() {

        TweetsReceivedCallback callback = new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsReceivedError() {

            }
        };

        TimelineRequest.builder()
                .apiUrl(FAVORITES_TIMELINE_PATH)
                .screenName(screenName)
                .callback(callback)
                .build()
                .execute();
    }

    @Override
    protected void getNextOldestTweets() {

        TweetsReceivedCallback callback = new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsReceivedError() {

            }
        };

        TimelineRequest.builder()
                .apiUrl(FAVORITES_TIMELINE_PATH)
                .screenName(screenName)
                .maxId(maxId)
                .callback(callback)
                .build()
                .execute();
    }
}
