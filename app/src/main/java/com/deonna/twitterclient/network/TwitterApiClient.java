package com.deonna.twitterclient.network;

import android.util.Log;

import com.deonna.twitterclient.BuildConfig;
import com.deonna.twitterclient.models.Tweet;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class TwitterApiClient {

    public static final String TAG = TwitterApiClient.class.getSimpleName();

    public static final String REST_URL = "https://api.twitter.com/1.1/";

    private static TwitterApiClient sInstance;

    private static final int DEFAULT_SINCE_ID = 1;
    private static final int DEFAULT_COUNT = 25;

    private OkHttpClient client;
    private Retrofit retrofit;
    private TwitterService service;

    private TwitterApiClient() {

        client = new OkHttpClient.Builder()
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String authentication = Credentials.basic(BuildConfig.API_KEY, BuildConfig.API_SECRET);
                        Request.Builder builder = response.request().newBuilder().header("Authorization", authentication);
                        return  builder.build();
                    }
                })
                .build();

        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(SchedulerProvider.io());

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(TwitterOauthClient.REST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        service = retrofit.create(TwitterService.class);
    }

    public static TwitterApiClient getInstance() {

        if (sInstance == null) {
            sInstance = new TwitterApiClient();
        }

        return sInstance;
    }

    public Observable<List<Tweet>> getHomeTimeline() {

        return service
                .getHomeTimeline(String.valueOf(DEFAULT_COUNT), String.valueOf(DEFAULT_SINCE_ID));
    }

    public void getTimeline(Callback<List<Tweet>> callback) {

        Call<List<Tweet>> call = service.getTimeline(String.valueOf(DEFAULT_COUNT), String.valueOf(DEFAULT_SINCE_ID));

        call.enqueue(callback);
    }
}
