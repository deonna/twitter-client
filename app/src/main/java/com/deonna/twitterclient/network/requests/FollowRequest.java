package com.deonna.twitterclient.network.requests;

import com.deonna.twitterclient.events.FollowCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FollowRequest implements TwitterRequest {

    public static final String KEY_SCREEN_NAME = "screen_name";
    public static final String KEY_FOLLOW = "follow";

    public static final String FOLLOW_PATH = "friendships/create.json";
    public static final String UNFOLLOW_PATH = "friendships/destroy.json";

    private String apiUrl;
    private FollowCallback callback;

    private RequestParams params;

    private FollowRequest() {

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

                User user = User.fromJsonSingle(response);

                callback.onFollow(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onFollowFailed();
            }
        };
    }

    private void setApiUrl(String apiUrl) {

        this.apiUrl = apiUrl;
    }

    private void setScreenName(String screenName) {

        params.put(KEY_SCREEN_NAME, screenName);
    }

    private void setFollow(Boolean follow) {

        params.put(KEY_FOLLOW, follow);
    }

    public void execute() {

        TwitterApplication.getRestClient().post(this);
    }

    /* Builder */

    public static Builder builder() {

        return new FollowRequest.Builder();
    }

    public static class Builder {

        private FollowRequest request = new FollowRequest();

        public Builder apiUrl(String apiUrl) {

            request.setApiUrl(apiUrl);

            return this;
        }

        public Builder screenName(String screenName) {

            request.setScreenName(screenName);

            return this;
        }

        public Builder follow(Boolean follow) {

            request.setFollow(follow);

            return this;
        }

        public Builder callback(FollowCallback callback) {

            request.callback = callback;

            return this;
        }

        public FollowRequest build() {

            return request;
        }
    }
}