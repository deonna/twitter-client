package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.widget.Toast;

import com.deonna.twitterclient.views.activities.TimelineActivity;
import com.deonna.twitterclient.events.callbacks.NewTweetsListener;
import com.deonna.twitterclient.events.callbacks.TweetSentCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

public class ComposeViewModel {

    private static final int IMAGE_SIZE = 36;
    public static final int INITIAL_CHARACTER_COUNT = 140;

    protected static final TwitterOauthClient client = TwitterApplication.getRestClient();

    protected NewTweetsListener context;
    protected User currentUser;

    public ComposeViewModel(NewTweetsListener context, User user) {

        currentUser = user;

        this.context = context;
    }

    public static int getImageSize() {

        return IMAGE_SIZE;
    }

    public int getCharacterCount(int count) {

        return INITIAL_CHARACTER_COUNT - count;
    }

    public void sendNewTweet(String newTweet) {

        client.sendNewTweet(newTweet, new TweetSentCallback() {

            @Override
            public void onTweetSent(String newTweet) {

                context.displayNewestTweets();
            }

            @Override
            public void onTweetSentFailed() {

                Toast.makeText((Context) context, "Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
