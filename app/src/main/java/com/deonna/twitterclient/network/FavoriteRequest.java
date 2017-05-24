package com.deonna.twitterclient.network;

import com.deonna.twitterclient.events.FavoriteCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FavoriteRequest implements TwitterRequest {

    public static final String KEY_ID = "id";

    public static final String FAVORITES_CREATE_ENDPOINT = "favorites/create.json";
    public static final String FAVORITES_DESTROY_ENDPOINT = "favorites/destroy.json";

    private String apiUrl;
    private FavoriteCallback callback;

    private RequestParams params;

    private FavoriteRequest() {

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

                callback.onFavorite(tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                callback.onFavoriteFailed();
            }
        };
    }

    private void setApiUrl(String apiUrl) {

        this.apiUrl = apiUrl;
    }

    private void setId(long id) {

        params.put(KEY_ID, id);
    }

    public void execute() {

        TwitterApplication.getRestClient().post(this);
    }

    /* Builder */

    public static Builder builder() {

        return new FavoriteRequest.Builder();
    }

    public static class Builder {

        private FavoriteRequest request = new FavoriteRequest();

        public Builder apiUrl(String apiUrl) {

            request.setApiUrl(apiUrl);

            return this;
        }

        public Builder id(long id) {

            request.setId(id);

            return this;
        }

        public Builder callback(FavoriteCallback callback) {

            request.callback = callback;

            return this;
        }

        public FavoriteRequest build() {

            return request;
        }
    }
}
