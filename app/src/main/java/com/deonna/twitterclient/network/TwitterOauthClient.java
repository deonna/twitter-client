package com.deonna.twitterclient.network;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.deonna.twitterclient.BuildConfig;
import com.deonna.twitterclient.callbacks.DirectMessageSentCallback;
import com.deonna.twitterclient.callbacks.DirectMessagesCallback;
import com.deonna.twitterclient.callbacks.FavoriteCallback;
import com.deonna.twitterclient.callbacks.RetweetCallback;
import com.deonna.twitterclient.callbacks.SearchResultsCallback;
import com.deonna.twitterclient.callbacks.TrendsCallback;
import com.deonna.twitterclient.callbacks.TweetSentCallback;
import com.deonna.twitterclient.callbacks.TweetsReceivedCallback;
import com.deonna.twitterclient.callbacks.UserInfoCallback;
import com.deonna.twitterclient.callbacks.UsersListCallback;
import com.deonna.twitterclient.models.DirectMessage;
import com.deonna.twitterclient.models.Trend;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.models.User;
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
    private static final String KEY_SINCE_ID = "since_id";
    private static final String KEY_ENTITIES = "include_entities";
    private static final String KEY_SCREEN_NAME = "screen_name";

    private static final String HOME_TIMELINE_PATH = "statuses/home_timeline.json";
    private static final String MENTIONS_TIMELINE_PATH = "statuses/mentions_timeline.json";
    private static final String USER_TIMELINE_PATH = "statuses/user_timeline.json";

    private static final int NUM_TWEETS_PER_FETCH = 25;
    private static final int DEFAULT_SINCE_ID = 1;
    public static final String FAVORITES_CREATE_ENDPOINT = "favorites/create.json";
    public static final String FAVORITES_DESTROY_ENDPOINT = "favorites/destroy.json";
    public static final String STATUS_UPDATE_ENDPOINT = "statuses/update.json";
    public static final String KEY_ID = "id";
    public static final String KEY_STATUS = "status";
    public static final String KEY_SKIP_STATUS = "skip_status";
    public static final String KEY_INCLUDE_EMAIL = "include_email";
    private static final String KEY_CURSOR = "cursor";
    private static final String KEY_TEXT = "text";

    public TwitterOauthClient(Context context) {

		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

	public void getHomeTimeline(final TweetsReceivedCallback callback) {

        fetchTimeline(HOME_TIMELINE_PATH, null, null, null, true, callback);
	}

	public void getMentionsTimeline(final TweetsReceivedCallback callback) {

        fetchTimeline(MENTIONS_TIMELINE_PATH, null, null, null, true, callback);
    }

	public void getUserTimeline(String screenName, final TweetsReceivedCallback callback) {

        fetchTimeline(USER_TIMELINE_PATH, screenName, null, null, true, callback);
	}

	public void getNextOldestTweets(Long maxId, final TweetsReceivedCallback callback) {

        fetchTimeline(HOME_TIMELINE_PATH, null, maxId, null, true, callback);
    }

    public void getNextOldestMentions(Long maxId, final TweetsReceivedCallback callback) {

        fetchTimeline(MENTIONS_TIMELINE_PATH, null, maxId, null, true, callback);
    }

    public void getNextOldestUserTimelineTweets(String screenName, Long maxId, TweetsReceivedCallback
            callback) {

        fetchTimeline(USER_TIMELINE_PATH, screenName, maxId, null, true, callback);
    }

    public void getNewestTweets(Long sinceId, TweetsReceivedCallback callback) {

        fetchTimeline(HOME_TIMELINE_PATH, null, null, sinceId, true, callback);
    }

	private void fetchTimeline(String apiUrl, String screenName, Long maxId, Long sinceId, Boolean entities, final TweetsReceivedCallback callback) {

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
        } else {
            params.put(KEY_ENTITIES, true);
        }

        getClient().get(getApiUrl(apiUrl), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                List<Tweet> tweets = Tweet.fromJsonMultiple(response);

                callback.onTweetsReceived(tweets);
                Tweet.saveAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onTweetsReceivedError();
            }
        });
    }

	public void getLoggedInUserInfo(UserInfoCallback callback ) {

        String apiUrl = getApiUrl("account/verify_credentials.json");

        RequestParams params = new RequestParams();
        params.put(KEY_SKIP_STATUS, true);
        params.put(KEY_INCLUDE_EMAIL, true);

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                User user = User.fromJsonSingle(response);

                callback.onUserInfoReceived(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onUserInfoError();
            }
        });
    }

	public void getUserInfo(String screenName, UserInfoCallback callback ) {

        String apiUrl = getApiUrl("users/show.json");

        RequestParams params = new RequestParams();
        params.put(KEY_SCREEN_NAME, screenName);
        params.put(KEY_INCLUDE_EMAIL, true);

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                User user = User.fromJsonSingle(response);

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

        String apiUrl = getApiUrl(STATUS_UPDATE_ENDPOINT);

        RequestParams params = new RequestParams();
        params.put(KEY_STATUS, newTweet);

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

        sendToFavoritesEndpoint(id, FAVORITES_CREATE_ENDPOINT, callback);

    }
    public void unfavoriteTweet(long id, FavoriteCallback callback) {

        sendToFavoritesEndpoint(id, FAVORITES_DESTROY_ENDPOINT, callback);
    }

    private void sendToFavoritesEndpoint(long id, String url, FavoriteCallback callback) {

        String apiUrl = getApiUrl(url);

        RequestParams params = new RequestParams();
        params.put(KEY_ID, id);

        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Tweet tweet = Tweet.fromJsonSingle(response);

                callback.onFavorite(tweet);
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

    public void getFollowersList(String screenName, Long cursor, UsersListCallback callback) {

        String apiUrl = getApiUrl("followers/list.json");

        RequestParams params = new RequestParams();

        if (cursor != null) {
            params.put("cursor", cursor);
        }

        params.put("screen_name", screenName.toLowerCase());
        params.put("skip_status", true);
        params.put("include_user_entities", false);

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    JSONArray usersArray = response.getJSONArray("users");
                    Long nextCursor = response.getLong("next_cursor");

                    List<User> users = User.fromJsonMultiple(usersArray);

                    callback.onUsersReceived(users, nextCursor);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void getFollowingList(String screenName, Long cursor, UsersListCallback callback) {

        String apiUrl = getApiUrl("friends/list.json");

        RequestParams params = new RequestParams();

        if (cursor != null) {
            params.put("cursor", cursor);
        }

        params.put("screen_name", screenName.toLowerCase());
        params.put("skip_status", true);
        params.put("include_user_entities", false);

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    JSONArray usersArray = response.getJSONArray("users");
                    Long nextCursor = response.getLong("next_cursor");

                    List<User> users = User.fromJsonMultiple(usersArray);

                    callback.onUsersReceived(users, nextCursor);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onUsersReceivedError();
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

    public void getDirectMessagesReceived(Long maxId, DirectMessagesCallback callback) {

        String apiUrl = getApiUrl("direct_messages.json");

        RequestParams params = new RequestParams();
        params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);

        if (maxId != null) {
            params.put(KEY_MAX_ID, maxId);
        }

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                List<DirectMessage> directMessages = DirectMessage.fromJsonMultiple(response);

                callback.onDirectMessagesReceived(directMessages);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onDirectMessagesError();
            }
        });
    }

    public void getDirectMessagesSent(Long maxId, DirectMessagesCallback callback) {

        String apiUrl = getApiUrl("direct_messages/sent.json");

        RequestParams params = new RequestParams();
        params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);

        if (maxId != null) {
            params.put(KEY_MAX_ID, maxId);
        }

        getClient().get(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                List<DirectMessage> directMessages = DirectMessage.fromJsonMultiple(response);

                callback.onDirectMessagesReceived(directMessages);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onDirectMessagesError();
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
