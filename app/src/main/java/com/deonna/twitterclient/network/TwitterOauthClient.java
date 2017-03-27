package com.deonna.twitterclient.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.deonna.twitterclient.BuildConfig;
import com.deonna.twitterclient.callbacks.FavoriteCallback;
import com.deonna.twitterclient.callbacks.RetweetCallback;
import com.deonna.twitterclient.callbacks.TweetSentCallback;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.callbacks.UserInfoCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

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
    private static final String KEY_ENTITIES = "include_entities";
    private static final String KEY_SCREEN_NAME = "screen_name";

    private static final String HOME_TIMELINE_PATH = "statuses/home_timeline.json";
    private static final String MENTIONS_TIMELINE_PATH = "statuses/mentions_timeline.json";
    private static final String USER_TIMELINE_PATH = "statuses/user_timeline.json";

    private static final int NUM_TWEETS_PER_FETCH = 25;
    private static final int DEFAULT_SINCE_ID = 1;

	public TwitterOauthClient(Context context) {

		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

	public void getHomeTimeline(final TweetsCallback callback) {

        fetchTimeline(HOME_TIMELINE_PATH, null, null, null, true, callback);
	}

	public void getMentionsTimeline(final TweetsCallback callback) {

        fetchTimeline(MENTIONS_TIMELINE_PATH, null, null, null, true, callback);
    }

	public void getUserTimeline(String screenName, final TweetsCallback callback) {

        fetchTimeline(USER_TIMELINE_PATH, screenName, null, null, true, callback);
	}

	public void getNextOldestTweets(Long maxId, final TweetsCallback callback) {

        fetchTimeline(HOME_TIMELINE_PATH, null, maxId, null, true, callback);
    }

    public void getNextOldestMentions(Long maxId, final TweetsCallback callback) {

        fetchTimeline(MENTIONS_TIMELINE_PATH, null, maxId, null, true, callback);
    }

    public void getNextOldestUserTimelineTweets(String screenName, Long maxId, TweetsCallback
            callback) {

        fetchTimeline(USER_TIMELINE_PATH, screenName, maxId, null, true, callback);
    }

    public void getNewestTweets(Long sinceId, TweetsCallback callback) {

        fetchTimeline(HOME_TIMELINE_PATH, null, null, sinceId, true, callback);
    }

	private void fetchTimeline(String apiUrl, String screenName, Long maxId, Long sinceId, Boolean entities, final TweetsCallback callback) {

        RequestParams params = new RequestParams();
        params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);

        if (screenName != null) {
            params.put(KEY_SCREEN_NAME, screenName);
        }

        if (maxId != null) {
            params.put(KEY_MAX_ID, maxId);
        }

        if (sinceId != null) {
            params.put(KEY_SINCE_ID, sinceId);
        } else {
            params.put(KEY_SINCE_ID, DEFAULT_SINCE_ID);
        }

        if (entities != null) {
            params.put(KEY_ENTITIES, entities);
        }

        getClient().get(getApiUrl(apiUrl), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                List<Tweet> tweets = Tweet.fromJson(response);

                callback.onTweetsReceived(tweets);
                Tweet.saveAll(tweets);
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

                User user = User.fromJson(response);

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

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onTweetSentFailed();
            }
        });
    }

    public void favoriteTweet(long id, FavoriteCallback callback) {

        sendToFavoritesEndpoint(id, "favorites/create.json", callback);

    }
    public void unfavoriteTweet(long id, FavoriteCallback callback) {

        sendToFavoritesEndpoint(id, "favorites/destroy.json", callback);
    }

    private void sendToFavoritesEndpoint(long id, String url, FavoriteCallback callback) {

        String apiUrl = getApiUrl(url);

        RequestParams params = new RequestParams();
        params.put("id", id);

        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                callback.onFavorite();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onFavoriteFailed();
            }
        });
    }


    public void retweet(long id, RetweetCallback callback) {

        String apiUrl = getApiUrl("statuses/retweet/" + id + ".json");

        RequestParams params = new RequestParams();
        params.put("id", id);

        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                callback.onRetweet();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onRetweetFailed();
            }
        });
    }


}
