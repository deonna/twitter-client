package com.deonna.twitterclient.viewmodels;

import android.content.Context;

import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.SchedulerProvider;
import com.deonna.twitterclient.network.TwitterClient;

import java.util.List;

import io.reactivex.Observable;


public class TimelineViewModel {

    private static final String TAG = com.deonna.twitterclient.models.TimelineViewModel.class.getSimpleName();

    private final Context context;
    private final TwitterClient client;

    public TimelineViewModel(Context context, TwitterClient client) {

        this.context = context;
        this.client = client;
    }

    public Observable<List<Tweet>> getHomeTimeline() {

        return client
                .getHomeTimeline()
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui());
    }
}
