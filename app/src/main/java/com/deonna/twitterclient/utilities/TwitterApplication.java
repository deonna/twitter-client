package com.deonna.twitterclient.utilities;

import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

import android.app.Application;
import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterOauthClient client = TwitterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApplication extends Application {

	private static Context context;
    private static User currentUser;

    public static void setCurrentUser(User currentUser) {
        TwitterApplication.currentUser = currentUser;
    }

    @Override
	public void onCreate() {

		super.onCreate();

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		TwitterApplication.context = this;
	}

	public static TwitterOauthClient getRestClient() {

		return (TwitterOauthClient) TwitterOauthClient.getInstance(TwitterOauthClient.class, TwitterApplication.context);
	}
}