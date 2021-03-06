package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.Times;
import com.deonna.twitterclient.utilities.TwitterApplication;

public class TweetViewModel extends BaseObservable {

    private static final int IMAGE_SIZE = 96;

    private static final String SCREEN_NAME_FORMAT = "@%s";

    protected Context context;
    protected Tweet tweet;

    protected Drawable retweetIcon;
    protected Drawable favoriteIcon;

    protected final TwitterOauthClient client;

    public TweetViewModel(Context context, Tweet tweet) {

        this.context = context;
        this.tweet = tweet;

        setRetweetIcon(tweet.retweeted);
        setFavoriteIcon(tweet.favorited);

        client = TwitterApplication.getRestClient();
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

    public int getMediaVisibility() {

        return tweet.getMediaUrl().isEmpty() ? View.GONE : View.VISIBLE;
    }

    public String getMediaUrl() {

        return tweet.getMediaUrl();
    }

    public String getScreenName() {

        return String.format(SCREEN_NAME_FORMAT, tweet.user.screenName);
    }

    public String getRelativeTimestamp() {

        return Times.getRelativeTimestamp(tweet.createdAt);
    }

    public String getTweetText() {

        return tweet.text;
    }

    public String getRetweetCount() {

        return (tweet.retweetCount.equals("0")) ? "" : tweet.retweetCount;
    }

    public String getFavoriteCount() {

        return (tweet.favoriteCount.equals("0")) ? "" : tweet.favoriteCount;
    }

    public static int getProfileImageSize() {

        return IMAGE_SIZE;
    }

    public Drawable getRetweetIcon() {

        return retweetIcon;
    }

    public void setRetweetIcon(boolean retweeted) {

        if (retweeted) {
            retweetIcon = context.getDrawable(R.drawable.ic_retweet_selected);
        } else {
            retweetIcon = context.getDrawable(R.drawable.ic_retweet);
        }
    }

    public Drawable getFavoriteIcon() {

        return favoriteIcon;
    }

    public void setFavoriteIcon(boolean favorited) {

        if (favorited) {
            favoriteIcon = context.getDrawable(R.drawable.ic_favorite_selected);
        } else {
            favoriteIcon = context.getDrawable(R.drawable.ic_favorite);
        }
    }
}
