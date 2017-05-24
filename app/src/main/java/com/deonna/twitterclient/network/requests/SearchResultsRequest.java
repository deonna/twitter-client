package com.deonna.twitterclient.network.requests;

import com.deonna.twitterclient.events.SearchResultsCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchResultsRequest implements TwitterRequest {

    private static final String KEY_COUNT = "count";
    private static final String KEY_MAX_ID = "max_id";
    private static final String KEY_QUERY = "q";

    private static final int NUM_TWEETS_PER_FETCH = 25;

    public static final String SEARCH_PATH = "search/tweets.json";

    private String apiUrl;
    private SearchResultsCallback callback;

    private RequestParams params;

    private SearchResultsRequest() {

        params = new RequestParams();
        params.put(KEY_COUNT, NUM_TWEETS_PER_FETCH);

        apiUrl = SEARCH_PATH;
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
        };
    }

    private void setMaxId(Long maxId) {

        if (maxId != null) {
            params.remove(KEY_MAX_ID);
            params.put(KEY_MAX_ID, maxId);
        }
    }

    private void setQuery(String query) {

        params.put(KEY_QUERY, query);
    }

    public void execute() {

        TwitterApplication.getRestClient().fetch(this);
    }

    /* Builder */

    public static Builder builder() {

        return new SearchResultsRequest.Builder();
    }

    public static class Builder {

        private SearchResultsRequest request = new SearchResultsRequest();

        public Builder query(String query) {

            request.setQuery(query);

            return this;
        }

        public Builder maxId(Long maxId) {

            request.setMaxId(maxId);

            return this;
        }

        public Builder callback(SearchResultsCallback callback) {

            request.callback = callback;

            return this;
        }

        public SearchResultsRequest build() {

            return request;
        }
    }
}
