package com.deonna.twitterclient.network;

import com.deonna.twitterclient.events.UserInfoCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserInfoRequest implements TwitterRequest {

    public static final String VERIFY_CREDENTIALS_PATH = "account/verify_credentials.json";
    public static final String SHOW_USER_PATH = "users/show.json";

    private static final String KEY_INCLUDE_EMAIL = "include_email";
    private static final String KEY_SKIP_STATUS = "skip_status";
    private static final String KEY_SCREEN_NAME = "screen_name";

    private String apiUrl;
    private UserInfoCallback callback;

    private RequestParams params;

    private UserInfoRequest() {

        params = new RequestParams();

        params.put(KEY_INCLUDE_EMAIL, true);
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

                callback.onUserInfoReceived(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onUserInfoError();
            }
        };
    }

    public void execute() {

        TwitterApplication.getRestClient().fetch(this);
    }

    private void setApiUrl(String apiUrl) {

        this.apiUrl = apiUrl;
    }

    private void setSkipStatus(Boolean skipStatus) {

        params.put(KEY_SKIP_STATUS, skipStatus);
    }

    private void setScreenName(String screenName) {

        params.put(KEY_SCREEN_NAME, screenName);
    }

    private void setIncludeEmail(Boolean includeEmail) {

        params.put(KEY_INCLUDE_EMAIL, includeEmail);
    }

    /* Builder */

    public static Builder builder() {

        return new UserInfoRequest.Builder();
    }

    public static class Builder {

        private UserInfoRequest request = new UserInfoRequest();

        public Builder apiUrl(String apiUrl) {

            request.setApiUrl(apiUrl);

            return this;
        }

        public Builder skipStatus(Boolean skipStatus) {

            request.setSkipStatus(skipStatus);

            return this;
        }

        public Builder includeEmail(Boolean includeEmail) {

            request.setIncludeEmail(includeEmail);

            return this;
        }

        public Builder screenName(String screenName) {

            request.setScreenName(screenName);

            return this;
        }

        public Builder callback(UserInfoCallback callback) {

            request.callback = callback;

            return this;
        }

        public UserInfoRequest build() {

            return request;
        }
    }
}
