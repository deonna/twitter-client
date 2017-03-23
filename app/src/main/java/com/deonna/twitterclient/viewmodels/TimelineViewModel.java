package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.deonna.twitterclient.TwitterApplication;
import com.deonna.twitterclient.activities.TimelineActivity;
import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class TimelineViewModel implements ViewModel {

    private static final String TAG = TimelineViewModel.class.getSimpleName();
    private static final String ERROR = "Error loading Tweets!";

    private final Context context;

    private final List<Tweet> tweets;
    private final TweetsAdapter tweetsAdapter;

    public TimelineViewModel(Context context) {

        this.context = context;

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(context, tweets);
    }

    @Override
    public void onCreate() {

        getHomeTimeline();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public TweetsAdapter getAdapter() {

        return tweetsAdapter;
    }

    public void getHomeTimeline() {

        TwitterApplication.getRestClient().getHomeTimeline(new TweetsCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {
                Log.d(TAG, newTweets.toString());
                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTweetsError() {

            }
        });
    }
}
