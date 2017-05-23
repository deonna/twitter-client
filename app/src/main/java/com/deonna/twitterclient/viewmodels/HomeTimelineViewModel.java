package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.deonna.twitterclient.events.TweetsReceivedCallback;
import com.deonna.twitterclient.views.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.NetworkStatus;

import java.util.List;

public class HomeTimelineViewModel extends TweetsTimelineViewModel {

    public HomeTimelineViewModel(Context context, FragmentManager fragmentManager, TweetsListFragment fragment) {

        super(context, fragmentManager, fragment);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (NetworkStatus.isNetworkAvailable(context)) {
            super.clearDb();
            getHomeTimeline();
        } else {
            super.loadTweetsFromDb();
        }
    }

    public void getHomeTimeline() {

        client.getHomeTimeline(new TweetsReceivedCallback() {

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
}
