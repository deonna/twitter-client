package com.deonna.twitterclient.network.requests;

import com.deonna.twitterclient.events.DirectMessagesCallback;
import com.deonna.twitterclient.models.DirectMessage;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DirectMessagesRequest implements TwitterRequest {

    public static final int NUM_TWEETS_PER_FETCH = 25;

    public static final String KEY_MAX_ID = "max_id";
    public static final String KEY_COUNT = "count";

    public static final String RECIEVED_DIRECT_MESSAGES_PATH = "direct_messages.json";
    public static final String SENT_DIRECT_MESSAGES_PATH = "direct_messages/sent.json";

    private String apiUrl;
    private DirectMessagesCallback callback;

    private RequestParams params;

    private DirectMessagesRequest() {

        params = new RequestParams();
        params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);
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

                List<DirectMessage> directMessages = DirectMessage.fromJsonMultiple(response);

                callback.onDirectMessagesReceived(directMessages);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onDirectMessagesError();
            }
        };
    }

    private void setApiUrl(String apiUrl) {

        this.apiUrl = apiUrl;
    }

    private void setMaxId(Long maxId) {

        if (maxId != null) {
            params.put(KEY_MAX_ID, maxId);
        }
    }

    public void execute() {

        TwitterApplication.getRestClient().fetch(this);
    }

    /* Builder */

    public static Builder builder() {

        return new DirectMessagesRequest.Builder();
    }

    public static class Builder {

        private DirectMessagesRequest request = new DirectMessagesRequest();

        public Builder apiUrl(String apiUrl) {

            request.setApiUrl(apiUrl);

            return this;
        }

        public Builder maxId(Long maxId) {

            request.setMaxId(maxId);

            return this;
        }

        public Builder callback(DirectMessagesCallback callback) {

            request.callback = callback;

            return this;
        }

        public DirectMessagesRequest build() {

            return request;
        }
    }
}