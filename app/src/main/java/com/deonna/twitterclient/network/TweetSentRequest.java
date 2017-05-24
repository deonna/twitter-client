package com.deonna.twitterclient.network;

import com.deonna.twitterclient.events.TweetSentCallback;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TweetSentRequest implements TwitterRequest {

    public static final String KEY_STATUS = "status";

    public static final String STATUS_UPDATE_ENDPOINT = "statuses/update.json";

    private String apiUrl;
    private TweetSentCallback callback;

    private String newTweet;

    private RequestParams params;

    private TweetSentRequest() {

        params = new RequestParams();

        apiUrl = STATUS_UPDATE_ENDPOINT;
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
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                callback.onTweetSent(newTweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onTweetSentFailed();
            }
        };
    }

    private void setStatus(String newTweet) {

        this.newTweet = newTweet;

        params.put(KEY_STATUS, newTweet);
    }

    public void execute() {

        TwitterApplication.getRestClient().post(this);
    }

    /* Builder */

    public static Builder builder() {

        return new TweetSentRequest.Builder();
    }

    public static class Builder {

        private TweetSentRequest request = new TweetSentRequest();

        public Builder status(String newTweet) {

            request.setStatus(newTweet);

            return this;
        }

        public Builder callback(TweetSentCallback callback) {

            request.callback = callback;

            return this;
        }

        public TweetSentRequest build() {

            return request;
        }
    }
}
