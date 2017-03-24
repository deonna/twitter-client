package com.deonna.twitterclient.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.deonna.twitterclient.BuildConfig;
import com.deonna.twitterclient.callbacks.TweetSentCallback;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.callbacks.UserInfoCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TwitterOauthClient extends OAuthBaseClient {

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.API_KEY;       // Change this
	public static final String REST_CONSUMER_SECRET = BuildConfig.API_SECRET; // Change this
	public static final String REST_CALLBACK_URL = "oauth://deonnatwitterclient"; // Change this (here and in manifest)

    private static final String KEY_COUNT = "count";
    private static final String KEY_MAX_ID = "max_id";
    private static final String KEY_SINCE_ID = "since_id";

    private static final int NUM_TWEETS_PER_FETCH = 25;

    private static Gson gson;

	public TwitterOauthClient(Context context) {

		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

	public void getHomeTimeline(final TweetsCallback callback) {

		RequestParams params = new RequestParams();
		params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);
		params.put(KEY_SINCE_ID, 1);

        fetchTimeline(params, null, callback);
	}

	public void getNextOldestTweets(Long maxId, final TweetsCallback callback) {

        RequestParams params = new RequestParams();
        params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);
        params.put(KEY_SINCE_ID, 1);

        fetchTimeline(params, maxId, callback);
    }

	private void fetchTimeline(RequestParams params, Long maxId, final TweetsCallback callback) {

        String apiUrl = getApiUrl("statuses/home_timeline.json");

        if (maxId != null) {
            params.put(KEY_MAX_ID, maxId);
        }

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Gson gson = new GsonBuilder().create();
                List<Tweet> tweets = gson.fromJson(
                        response.toString(),
                        new TypeToken<ArrayList<Tweet>>() {
                        }.getType());

                callback.onTweetsReceived(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onTweetsError();
            }
        });
    }

	public void getLoggedInUserInfo(UserInfoCallback callback ) {

        String apiUrl = getApiUrl("account/verify_credentials.json");

        RequestParams params = new RequestParams();
        params.put("skip_status", true);
        params.put("include_email", true);

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Gson gson = new GsonBuilder().create();
                User user = gson.fromJson(
                        response.toString(),
                        new TypeToken<User>() {
                        }.getType());

                callback.onUserInfoReceived(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onUserInfoError();
            }
        });
    }

    public void sendNewTweet(String newTweet, TweetSentCallback callback) {

        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        params.put("status", newTweet);

        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                callback.onTweetSent(newTweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onTweetSentFailed();
            }
        });

    }
}
