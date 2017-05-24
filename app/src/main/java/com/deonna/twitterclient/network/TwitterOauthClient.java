package com.deonna.twitterclient.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.deonna.twitterclient.BuildConfig;
import com.deonna.twitterclient.events.DirectMessageSentCallback;
import com.deonna.twitterclient.events.RetweetCallback;
import com.deonna.twitterclient.events.SearchResultsCallback;
import com.deonna.twitterclient.events.TrendsCallback;
import com.deonna.twitterclient.models.Trend;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.requests.TwitterRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
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
    private static final String KEY_SCREEN_NAME = "screen_name";

    private static final int NUM_TWEETS_PER_FETCH = 25;
    public static final String KEY_ID = "id";
    private static final String KEY_TEXT = "text";

    public TwitterOauthClient(Context context) {

		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void fetch(TwitterRequest request) {

        getClient().get(
                getApiUrl(request.getPath()),
                request.getParams(),
                request.getHandler()
        );
    }

    public void post(TwitterRequest request) {

        getClient().post(
            getApiUrl(request.getPath()),
            request.getParams(),
            request.getHandler()
        );
    }

    public void logOut() {
        super.clearAccessToken();
    }

    public void retweet(long id, RetweetCallback callback) {

        String apiUrl = getApiUrl("statuses/retweet/" + id + ".json");

        RequestParams params = new RequestParams();
        params.put(KEY_ID, id);

        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Tweet tweet = Tweet.fromJsonSingle(response);

                callback.onRetweet(tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onRetweetFailed();
            }
        });
    }

    public void getSearchResults(String hashtag, Long maxId, SearchResultsCallback callback) {

        String apiUrl = getApiUrl("search/tweets.json");

        RequestParams params = new RequestParams();
        params.put("q", hashtag);
        params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);

        if (maxId != null) {
            params.put(KEY_MAX_ID, maxId);
        }

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    JSONArray tweetsArray = response.getJSONArray("statuses");

                    List<Tweet> tweets = Tweet.fromJsonMultiple(tweetsArray);

                    callback.onSearchResultsReceived(tweets);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onSearchResultsError();
            }
        });
    }

    public void sendDirectMessage(String screenName, String text, DirectMessageSentCallback callback) {

        String apiUrl = getApiUrl("direct_messages/new.json");

        RequestParams params = new RequestParams();
        params.put(KEY_SCREEN_NAME, screenName);
        params.put(KEY_TEXT, text);

        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                callback.onDirectMessageSent();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onDirectMessageSentFailed();
            }
        });
    }

    public void getTrends(TrendsCallback callback) {

        String apiUrl = getApiUrl("trends/place.json");

        final int WOEID = 1;

        RequestParams params = new RequestParams();
        params.put(KEY_ID, WOEID);

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                List<Trend> trends = new ArrayList<Trend>();

                callback.onTrendsRecieved(trends);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onTrendsError();
            }
        });

    }
}
