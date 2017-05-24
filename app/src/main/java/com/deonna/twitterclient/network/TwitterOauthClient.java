package com.deonna.twitterclient.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.deonna.twitterclient.BuildConfig;
import com.deonna.twitterclient.events.DirectMessageSentCallback;
import com.deonna.twitterclient.events.DirectMessagesCallback;
import com.deonna.twitterclient.events.FavoriteCallback;
import com.deonna.twitterclient.events.FollowCallback;
import com.deonna.twitterclient.events.RetweetCallback;
import com.deonna.twitterclient.events.SearchResultsCallback;
import com.deonna.twitterclient.events.TrendsCallback;
import com.deonna.twitterclient.events.TweetSentCallback;
import com.deonna.twitterclient.events.TweetsReceivedCallback;
import com.deonna.twitterclient.events.UserInfoCallback;
import com.deonna.twitterclient.events.UsersListCallback;
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

import static com.deonna.twitterclient.network.FavoriteRequest.FAVORITES_CREATE_ENDPOINT;
import static com.deonna.twitterclient.network.FavoriteRequest.FAVORITES_DESTROY_ENDPOINT;
import static com.deonna.twitterclient.network.TimelineRequest.FAVORITES_TIMELINE_PATH;
import static com.deonna.twitterclient.network.TimelineRequest.HOME_TIMELINE_PATH;
import static com.deonna.twitterclient.network.TimelineRequest.MENTIONS_TIMELINE_PATH;
import static com.deonna.twitterclient.network.TimelineRequest.USER_TIMELINE_PATH;
import static com.deonna.twitterclient.network.UserInfoRequest.SHOW_USER_PATH;
import static com.deonna.twitterclient.network.UserInfoRequest.VERIFY_CREDENTIALS_PATH;
import static com.deonna.twitterclient.network.UsersListRequest.SHOW_FOLLOWERS_PATH;
import static com.deonna.twitterclient.network.UsersListRequest.SHOW_FOLLOWING_PATH;

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
    public static final String STATUS_UPDATE_ENDPOINT = "statuses/update.json";
    public static final String KEY_ID = "id";
    public static final String KEY_STATUS = "status";
    private static final String KEY_TEXT = "text";

    public TwitterOauthClient(Context context) {

		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    protected void fetch(TwitterRequest request) {

        getClient().get(
                getApiUrl(request.getPath()),
                request.getParams(),
                request.getHandler()
        );
    }

    protected void post(TwitterRequest request) {

        getClient().post(
            getApiUrl(request.getPath()),
            request.getParams(),
            request.getHandler()
        );
    }

    public void logOut() {
        super.clearAccessToken();
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

        FavoriteRequest request = FavoriteRequest.builder()
                .apiUrl(FAVORITES_CREATE_ENDPOINT)
                .id(id)
                .callback(callback)
                .build();

        request.execute();
    }
    public void unfavoriteTweet(long id, FavoriteCallback callback) {

        FavoriteRequest request = FavoriteRequest.builder()
                .apiUrl(FAVORITES_DESTROY_ENDPOINT)
                .id(id)
                .callback(callback)
                .build();

        request.execute();
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

        UsersListRequest request = UsersListRequest.builder()
                .apiUrl(SHOW_FOLLOWERS_PATH)
                .cursor(cursor)
                .screenName(screenName)
                .callback(callback)
                .build();

        request.execute();
    }

    public void getFollowingList(String screenName, Long cursor, UsersListCallback callback) {

        UsersListRequest request = UsersListRequest.builder()
                .apiUrl(SHOW_FOLLOWING_PATH)
                .cursor(cursor)
                .screenName(screenName)
                .callback(callback)
                .build();

        request.execute();
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

    public void follow(String screenName, FollowCallback callback) {

        String apiUrl = getApiUrl("friendships/create.json");

        RequestParams params = new RequestParams();
        params.put(KEY_SCREEN_NAME, screenName);
        params.put("follow", true);

        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                User user = User.fromJsonSingle(response);

                callback.onFollow(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onFollowFailed();
            }
        });
    }

    public void unfollow(String screenName, FollowCallback callback) {

        String apiUrl = getApiUrl("friendships/destroy.json");

        RequestParams params = new RequestParams();
        params.put(KEY_SCREEN_NAME, screenName);

        getClient().post(apiUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                User user = User.fromJsonSingle(response);

                callback.onFollow(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onFollowFailed();
            }
        });
    }
}
