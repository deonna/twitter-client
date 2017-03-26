package com.deonna.twitterclient.viewmodels;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

public class MentionsTimelineViewModel extends TweetsListViewModel {

    public MentionsTimelineViewModel(Context context, FragmentManager fragmentManager) {

        super(context, fragmentManager);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getMentionsTimeline();
    }

    public void getMentionsTimeline() {

        client.getMentionsTimeline(new TweetsCallback() {

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

        client.getNextOldestMentions(maxId, new TweetsCallback() {

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
