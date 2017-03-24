package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.widget.Toast;

import com.deonna.twitterclient.callbacks.TweetSentCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

public class ComposeViewModel {

    private static final int IMAGE_SIZE = 36;
    private static final int INITIAL_CHARACTER_COUNT = 140;

    private static final TwitterOauthClient client = TwitterApplication.getRestClient();


    private Context context;
    private User currentUser;

    public ComposeViewModel(Context context, User user) {

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

                //TODO: Need to refresh timeline
            }

            @Override
            public void onTweetSentFailed() {

                Toast.makeText(context, "Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
