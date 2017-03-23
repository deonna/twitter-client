package com.deonna.twitterclient.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.deonna.twitterclient.BuildConfig;
import com.deonna.twitterclient.models.Tweet;
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
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class TwitterOauthClient extends OAuthBaseClient {

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.API_KEY;       // Change this
	public static final String REST_CONSUMER_SECRET = BuildConfig.API_SECRET; // Change this
	public static final String REST_CALLBACK_URL = "oauth://deonnatwitterclient"; // Change this (here and in manifest)

	public TwitterOauthClient(Context context) {

		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public Observable<List<Tweet>> getHomeTimeline() {
		String apiUrl = getApiUrl("statuses/home_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);

		getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Gson gson = new GsonBuilder().create();
                List<Tweet> tweets = gson.fromJson(
                        response.toString(),
                        new TypeToken<ArrayList<Tweet>>() {
                        }.getType());

                //callback.onTweetsReceived(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
	}
}
