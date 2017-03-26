package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;

import java.util.List;

public class HomeTimelineViewModel extends TweetsListViewModel {

    public HomeTimelineViewModel(Context context, FragmentManager fragmentManager) {

        super(context, fragmentManager);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getHomeTimeline();
    }

    public void getHomeTimeline() {

        client.getHomeTimeline(new TweetsCallback() {

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
}
