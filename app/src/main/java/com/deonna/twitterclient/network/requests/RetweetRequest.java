package com.deonna.twitterclient.network.requests;

import com.deonna.twitterclient.events.RetweetCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RetweetRequest implements TwitterRequest {

    public static final String KEY_ID = "id";

    public static final String RETWEET_ENDPOINT_FORMAT = "statuses/retweet/%s.json";

    private String apiUrl;
    private RetweetCallback callback;

    private RequestParams params;

    private RetweetRequest() {

        params = new RequestParams();
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

                Tweet tweet = Tweet.fromJsonSingle(response);

                callback.onRetweet(tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onRetweetFailed();
            }
        };
    }

    private void setId(long id) {

        params.put(KEY_ID, id);

        apiUrl = String.format(RETWEET_ENDPOINT_FORMAT, Long.valueOf(id));
    }

    public void execute() {

        TwitterApplication.getRestClient().post(this);
    }

    /* Builder */

    public static Builder builder() {

        return new RetweetRequest.Builder();
    }

    public static class Builder {

        private RetweetRequest request = new RetweetRequest();

        public Builder id(long id) {

            request.setId(id);

            return this;
        }

        public Builder callback(RetweetCallback callback) {

            request.callback = callback;

            return this;
        }

        public RetweetRequest build() {

            return request;
        }
    }
}

