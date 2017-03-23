package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.view.View;

import com.deonna.twitterclient.models.Tweet;

public class TweetViewModel {

    private static final int IMAGE_SIZE = 96;

    private static final String SCREEN_NAME_FORMAT = "@%s";

    private static final String SMALL_IMAGE_TEXT = "_normal";

    private Context context;
    private Tweet tweet;

    public TweetViewModel(Context context, Tweet tweet) {

        this.context = context;
        this.tweet = tweet;
    }

    public String getName() {

        return tweet.user.name;
    }

    public int getIsVerifiedVisibility() {

        if (tweet.user.isVerified) {

            return View.VISIBLE;
        }

        return View.GONE;
    }

    public String getScreenName() {

        return String.format(SCREEN_NAME_FORMAT, tweet.user.screenName);
    }

    public String getTweetText() {

        return tweet.text;
    }

    public String getRetweetCount() {

        return tweet.retweetCount;
    }

    public String getFavoriteCount() {

        return tweet.favoriteCount;
    }

    public String getLargeProfileImageUrl() {

        return  tweet.user.profileImageUrl.replaceAll(SMALL_IMAGE_TEXT, "");
    }

    public static int getProfileImageSize() {

        return IMAGE_SIZE;
    }
}
