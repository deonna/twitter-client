package com.deonna.twitterclient.network.requests;

import com.deonna.twitterclient.events.DirectMessageSentCallback;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DirectMessageSentRequest implements TwitterRequest {

    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_TEXT = "text";

    public static final String SEND_DIRECT_MESSAGE_PATH = "direct_messages/new.json";

    private String apiUrl;
    private DirectMessageSentCallback callback;

    private RequestParams params;

    private DirectMessageSentRequest() {

        params = new RequestParams();

        apiUrl = SEND_DIRECT_MESSAGE_PATH;
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

                callback.onDirectMessageSent();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onDirectMessageSentFailed();
            }
        };
    }

    private void setScreenName(String screenName) {

        params.put(KEY_SCREEN_NAME, screenName);
    }

    private void setText(String text) {

        params.put(KEY_TEXT, text);
    }

    public void execute() {

        TwitterApplication.getRestClient().post(this);
    }

    /* Builder */

    public static Builder builder() {

        return new DirectMessageSentRequest.Builder();
    }

    public static class Builder {

        private DirectMessageSentRequest request = new DirectMessageSentRequest();

        public Builder screenName(String screenName) {

            request.setScreenName(screenName);

            return this;
        }

        public Builder text(String text) {

            request.setText(text);

            return this;
        }

        public Builder callback(DirectMessageSentCallback callback) {

            request.callback = callback;

            return this;
        }

        public DirectMessageSentRequest build() {

            return request;
        }
    }
}
