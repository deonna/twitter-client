package com.deonna.twitterclient.network.requests;

import com.deonna.twitterclient.events.TrendsCallback;
import com.deonna.twitterclient.models.Trend;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TrendsRequest implements TwitterRequest {

    public static final String KEY_ID = "id";

    public static final String GET_TRENDS_PATH = "trends/place.json";

    private String apiUrl;
    private TrendsCallback callback;

    private RequestParams params;

    private TrendsRequest() {

        params = new RequestParams();

        apiUrl = GET_TRENDS_PATH;

        params.put(KEY_ID, 1);
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

                List<Trend> trends = new ArrayList<Trend>();

                callback.onTrendsRecieved(trends);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                callback.onTrendsError();
            }
        };
    }

    private void setWoeid(int woeid) {

        params.remove(KEY_ID);
        params.put(KEY_ID, woeid);
    }

    public void execute() {

        TwitterApplication.getRestClient().fetch(this);
    }

    /* Builder */

    public static Builder builder() {

        return new TrendsRequest.Builder();
    }

    public static class Builder {

        private TrendsRequest request = new TrendsRequest();

        public Builder woied(int id) {

            request.setWoeid(id);

            return this;
        }

        public Builder callback(TrendsCallback callback) {

            request.callback = callback;

            return this;
        }

        public TrendsRequest build() {

            return request;
        }
    }
}
