package com.deonna.twitterclient.viewmodels;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.deonna.twitterclient.callbacks.TweetsReceivedCallback;
import com.deonna.twitterclient.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

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

        client.getMentionsTimeline(new TweetsReceivedCallback() {

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
        });
    }

    @Override
    protected void getNextOldestTweets() {

        client.getNextOldestMentions(maxId, new TweetsReceivedCallback() {

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
