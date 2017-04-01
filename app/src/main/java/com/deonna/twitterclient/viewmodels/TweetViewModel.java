package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.callbacks.FavoriteCallback;
import com.deonna.twitterclient.callbacks.RetweetCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TweetViewModel extends BaseObservable {

    private static final int IMAGE_SIZE = 96;

    private static final String SCREEN_NAME_FORMAT = "@%s";

    private static final String DATE_PATTERN = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

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

    public void retweet(long id, ImageView ivRetweetIcon, TextView tvRetweetCount) {

        client.retweet(id, new RetweetCallback() {
            @Override
            public void onRetweet(Tweet newTweet) {

                tweet = newTweet;
                setRetweetIcon(true);

                ivRetweetIcon.setImageDrawable(retweetIcon);
                tvRetweetCount.setText(getRetweetCount());
            }

            @Override
            public void onRetweetFailed() {

                setRetweetIcon(false);
            }
        });
    }

    public void favorite(long id, ImageView ivFavoriteIcon, TextView
            tvFavoriteCount) {

        client.favoriteTweet(id, new FavoriteCallback() {
            @Override
            public void onFavorite(Tweet newTweet) {

                tweet = newTweet;
                setFavoriteIcon(true);

                if (getFavoriteCount().equals("")) {
                    tweet.favoriteCount = "1";
                }

                ivFavoriteIcon.setImageDrawable(favoriteIcon);
                tvFavoriteCount.setText(getFavoriteCount());
            }

            @Override
            public void onFavoriteFailed() {

            }
        });
    }

    public void unfavorite(long id, ImageView ivFavoriteIcon, TextView
            tvFavoriteCount) {

        client.unfavoriteTweet(id, new FavoriteCallback() {
            @Override
            public void onFavorite(Tweet newTweet) {

                tweet = newTweet;
                setFavoriteIcon(false);

                ivFavoriteIcon.setImageDrawable(favoriteIcon);
                tvFavoriteCount.setText(getFavoriteCount());
            }

            @Override
            public void onFavoriteFailed() {

            }
        });
    }
}
