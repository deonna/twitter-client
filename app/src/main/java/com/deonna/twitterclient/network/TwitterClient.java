package com.deonna.twitterclient.network;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.os.Build;

import com.codepath.oauth.OAuthBaseClient;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.models.TwitterResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.deonna.twitterclient.BuildConfig;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class TwitterClient extends OAuthBaseClient {

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.API_KEY;       // Change this
	public static final String REST_CONSUMER_SECRET = BuildConfig.API_SECRET; // Change this
	public static final String REST_CALLBACK_URL = "oauth://deonnatwitterclient"; // Change this (here and in manifest)

    private static final int DEFAULT_SINCE_ID = 1;
    private static final int DEFAULT_COUNT = 25;

    private OkHttpClient client;
    private Retrofit retrofit;
    private TwitterService service;

	public TwitterClient(Context context) {

		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);

        client = new OkHttpClient.Builder()
                    .build();

        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(SchedulerProvider.io());

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(REST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        service = retrofit.create(TwitterService.class);
	}

    public Observable<List<Tweet>> getHomeTimeline() {

        return service.getHomeTimeline(String.valueOf(DEFAULT_COUNT), String.valueOf(DEFAULT_SINCE_ID));
    }
}
