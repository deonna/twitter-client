package com.deonna.twitterclient.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.deonna.twitterclient.BuildConfig;
import com.deonna.twitterclient.network.requests.TwitterRequest;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

public class TwitterOauthClient extends OAuthBaseClient {

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.API_KEY;       // Change this
	public static final String REST_CONSUMER_SECRET = BuildConfig.API_SECRET; // Change this
	public static final String REST_CALLBACK_URL = "oauth://deonnatwitterclient"; // Change this (here and in manifest)

    public TwitterOauthClient(Context context) {

		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void fetch(TwitterRequest request) {

        getClient().get(
                getApiUrl(request.getPath()),
                request.getParams(),
                request.getHandler()
        );
    }

    public void post(TwitterRequest request) {

        getClient().post(
            getApiUrl(request.getPath()),
            request.getParams(),
            request.getHandler()
        );
    }

    public void logOut() {
        super.clearAccessToken();
    }
}
