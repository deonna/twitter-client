package com.deonna.twitterclient.viewmodels;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.deonna.twitterclient.events.TweetsReceivedCallback;
import com.deonna.twitterclient.network.TimelineRequest;
import com.deonna.twitterclient.views.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

import static com.deonna.twitterclient.network.TimelineRequest.MENTIONS_TIMELINE_PATH;

public class MentionsTimelineViewModel extends TweetsTimelineViewModel {

    public MentionsTimelineViewModel(Context context, FragmentManager fragmentManager, TweetsListFragment fragment) {

        super(context, fragmentManager, fragment);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getMentionsTimeline();
    }

    public void getMentionsTimeline() {

        TweetsReceivedCallback callback = new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {
                Log.d(TAG, newTweets.toString());
                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsReceivedError() {

            }
        };

        TimelineRequest.builder()
                .apiUrl(MENTIONS_TIMELINE_PATH)
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
                .apiUrl(MENTIONS_TIMELINE_PATH)
                .maxId(maxId)
                .callback(callback)
                .build()
                .execute();
    }
}
