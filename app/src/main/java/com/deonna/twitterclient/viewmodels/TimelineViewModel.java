package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.util.Log;

import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.SchedulerProvider;
import com.deonna.twitterclient.network.TwitterApiClient;
import com.deonna.twitterclient.network.TwitterOauthClient;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TimelineViewModel {

    private static final String TAG = TimelineViewModel.class.getSimpleName();
    private static final String ERROR = "Error loading Tweets!";

    private final Context context;
    private final TwitterApiClient client;

    public TimelineViewModel(Context context, TwitterApiClient client) {

        this.context = context;
        this.client = client;
    }

    public Observable<List<Tweet>> getHomeTimeline() {

        return client
                .getHomeTimeline()
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnNext(list -> Log.d(TAG, "Success! " + list.toString()))
                .doOnError(error -> Log.e(TAG, "Error! " + error.toString()));
    }

    public void getTimeline() {

        client.getTimeline(new Callback<List<Tweet>>() {

            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {

                List<Tweet> tweets = response.body();

                Log.d(TAG, "Success! " + tweets);
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {

                Log.e(TAG, ERROR);
                t.printStackTrace();
            }
        });
    }
}
