package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.view.View;

import com.deonna.twitterclient.models.Tweet;

public class TweetViewModel {

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

        return String.format("@%s", tweet.user.screenName);
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
}
