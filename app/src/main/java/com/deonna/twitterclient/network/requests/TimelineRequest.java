package com.deonna.twitterclient.network.requests;

import com.deonna.twitterclient.events.TweetsReceivedCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineRequest implements TwitterRequest {

    public static final String HOME_TIMELINE_PATH = "statuses/home_timeline.json";
    public static final String MENTIONS_TIMELINE_PATH = "statuses/mentions_timeline.json";
    public static final String USER_TIMELINE_PATH = "statuses/user_timeline.json";
    public static final String FAVORITES_TIMELINE_PATH = "favorites/list.json";

    private static final int DEFAULT_SINCE_ID = 1;
    private static final int NUM_TWEETS_PER_FETCH = 25;

    private static final String KEY_COUNT = "count";
    private static final String KEY_MAX_ID = "max_id";
    private static final String KEY_SINCE_ID = "since_id";
    private static final String KEY_INCLUDE_ENTITIES = "include_entities";
    private static final String KEY_SCREEN_NAME = "screen_name";

    private String apiUrl;
    private TweetsReceivedCallback callback;

    private RequestParams params;

    private TimelineRequest() {

        params = new RequestParams();

        params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);
        params.put(KEY_SINCE_ID, DEFAULT_SINCE_ID);
        params.put(KEY_INCLUDE_ENTITIES, true);
    }

    @Override
    public String getPath() {

        return apiUrl;
    }

    @Override
    public RequestParams getParams() {

        return params;
    }

    @Override
    public JsonHttpResponseHandler getHandler() {

        return new JsonHttpResponseHandler() {

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
        };
    }

    public void execute() {

        TwitterApplication.getRestClient().fetch(this);
    }

    private void setNumTweetsPerFetch(int numTweetsPerFetch) {

        params.remove(KEY_COUNT);
        params.put(KEY_COUNT, numTweetsPerFetch);
    }

    private void setApiUrl(String apiUrl) {

        this.apiUrl = apiUrl;
    }

    private void setScreenName(String screenName) {

        params.put(KEY_SCREEN_NAME, screenName);
    }

    private void setMaxId(Long maxId) {

        params.remove(KEY_MAX_ID);
        params.put(KEY_MAX_ID, maxId);
    }

    private void setSinceId(Long sinceId) {

        params.remove(KEY_SINCE_ID);
        params.put(KEY_SINCE_ID, sinceId);
    }

    private void setIncludeEntities(Boolean includeEntities) {

        params.remove(KEY_INCLUDE_ENTITIES);
        params.put(KEY_INCLUDE_ENTITIES, includeEntities);
    }

    /* Builder */

    public static Builder builder() {

        return new TimelineRequest.Builder();
    }

    public static class Builder {

        private TimelineRequest request = new TimelineRequest();

        public Builder apiUrl(String apiUrl) {

            request.setApiUrl(apiUrl);

            return this;
        }

        public Builder screenName(String screenName) {

            request.setScreenName(screenName);

            return this;
        }

        public Builder maxId(Long maxId) {

            request.setMaxId(maxId);

            return this;
        }

        public Builder sinceId(Long sinceId) {

            request.setSinceId(sinceId);

            return this;
        }

        public Builder entities(Boolean includeEntities) {

            request.setIncludeEntities(includeEntities);

            return this;
        }

        public Builder callback(TweetsReceivedCallback callback) {

            request.callback = callback;

            return this;
        }

        public TimelineRequest build() {

            return request;
        }
    }
}
