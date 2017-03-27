package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.format.DateUtils;
import android.view.View;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.callbacks.FavoriteCallback;
import com.deonna.twitterclient.callbacks.RetweetCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TweetViewModel {

    private static final int IMAGE_SIZE = 96;

    private static final String SCREEN_NAME_FORMAT = "@%s";

    private static final String DATE_PATTERN = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    private Context context;
    private Tweet tweet;

    protected final TwitterOauthClient client;

    public TweetViewModel(Context context, Tweet tweet) {

        this.context = context;
        this.tweet = tweet;

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

        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
        format.setLenient(true);

        String relativeDate = "";

        try {
            long dateMillis = format.parse(tweet.createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
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

    public static int getProfileImageSize() {

        return IMAGE_SIZE;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Drawable getRetweetedDrawable() {

        if (tweet.retweeted) {
            return context.getDrawable(R.drawable.ic_retweet_selected);
        } else {
            return context.getDrawable(R.drawable.ic_retweet);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public Drawable getFavoritedDrawable() {
        if (tweet.retweeted) {
            return context.getDrawable(R.drawable.ic_favorite_selected);
        } else {
            return context.getDrawable(R.drawable.ic_favorite);
        }
    }

    public void retweet(long id) {

        client.retweet(id, new RetweetCallback() {
            @Override
            public void onRetweet() {

            }

            @Override
            public void onRetweetFailed() {

            }
        });
    }r

    public void favorite(long id) {

        client.favoriteTweet(id, new FavoriteCallback() {
            @Override
            public void onFavorite() {

            }

            @Override
            public void onFavoriteFailed() {

            }
        });
    }

    public void unfavorite(long id) {

        client.unfavoriteTweet(id, new FavoriteCallback() {
            @Override
            public void onFavorite() {

            }

            @Override
            public void onFavoriteFailed() {

            }
        });
    }
}
