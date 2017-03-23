package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.util.Log;

import com.deonna.twitterclient.TwitterApplication;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.SchedulerProvider;

import java.util.List;

import io.reactivex.Observable;


public class TimelineViewModel {

    private static final String TAG = TimelineViewModel.class.getSimpleName();
    private static final String ERROR = "Error loading Tweets!";

    private final Context context;

    public TimelineViewModel(Context context) {

        this.context = context;
    }

    public void getHomeTimeline() {

        TwitterApplication.getRestClient().getHomeTimeline(new TweetsCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> tweets) {
                Log.d(TAG, tweets.toString());

            }

            @Override
            public void onTweetsError() {

            }
        });
    }
}
