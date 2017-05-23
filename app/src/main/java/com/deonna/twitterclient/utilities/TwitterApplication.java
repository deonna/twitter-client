package com.deonna.twitterclient.utilities;

import com.crashlytics.android.Crashlytics;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;

import android.app.Application;
import android.content.Context;
import io.fabric.sdk.android.Fabric;
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

    public static User getCurrentUser() {
        return currentUser;
    }

    @Override
	public void onCreate() {

		super.onCreate();

		if (LeakCanary.isInAnalyzerProcess(this)) {
			return;
		}

		LeakCanary.install(this);

		Fabric.with(this, new Crashlytics());

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		TwitterApplication.context = this;
	}

	public static TwitterOauthClient getRestClient() {

		return (TwitterOauthClient) TwitterOauthClient.getInstance(TwitterOauthClient.class, TwitterApplication.context);
	}
}