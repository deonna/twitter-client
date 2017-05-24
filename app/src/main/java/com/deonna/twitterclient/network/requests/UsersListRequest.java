package com.deonna.twitterclient.network.requests;

import com.deonna.twitterclient.events.UsersListCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class UsersListRequest implements TwitterRequest {

    public static final String SHOW_FOLLOWERS_PATH = "followers/list.json";
    public static final String SHOW_FOLLOWING_PATH = "friends/list.json";

    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_SKIP_STATUS = "skip_status";
    private static final String KEY_INCLUDE_USER_ENTITIES = "include_user_entities";

    private static final String KEY_USERS = "users";
    private static final String KEY_CURSOR = "cursor";
    private static final String KEY_NEXT_CURSOR = "next_cursor";

    private String apiUrl;
    private UsersListCallback callback;

    private RequestParams params;

    private UsersListRequest() {

        params = new RequestParams();

        params.put(KEY_CURSOR, -1);
        params.put(KEY_SKIP_STATUS, true);
        params.put(KEY_INCLUDE_USER_ENTITIES, false);
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

                try {

                    JSONArray usersArray = response.getJSONArray(KEY_USERS);
                    Long nextCursor = response.getLong(KEY_NEXT_CURSOR);

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
        };
    }

    private void setApiUrl(String apiUrl) {

        this.apiUrl = apiUrl;
    }

    private void setScreenName(String screenName) {

        params.put(KEY_SCREEN_NAME, screenName);
    }

    private void setCursor(Long cursor) {

        if (cursor != null) {
            params.remove(KEY_CURSOR);
            params.put(KEY_CURSOR, cursor);
        }
    }

    public void execute() {

        TwitterApplication.getRestClient().fetch(this);
    }

    /* Builder */

    public static Builder builder() {

        return new UsersListRequest.Builder();
    }

    public static class Builder {

        private UsersListRequest request = new UsersListRequest();

        public Builder apiUrl(String apiUrl) {

            request.setApiUrl(apiUrl);

            return this;
        }

        public Builder screenName(String screenName) {

            request.setScreenName(screenName);

            return this;
        }

        public Builder cursor(Long cursor) {

            request.setCursor(cursor);

            return this;
        }

        public Builder callback(UsersListCallback callback) {

            request.callback = callback;

            return this;
        }

        public UsersListRequest build() {

            return request;
        }
    }
}
