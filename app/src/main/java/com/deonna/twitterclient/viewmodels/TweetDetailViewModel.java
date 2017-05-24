package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.widget.ImageView;

import com.deonna.twitterclient.events.FavoriteCallback;
import com.deonna.twitterclient.events.RetweetCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.FavoriteRequest;

import static com.deonna.twitterclient.network.FavoriteRequest.FAVORITES_CREATE_ENDPOINT;
import static com.deonna.twitterclient.network.FavoriteRequest.FAVORITES_DESTROY_ENDPOINT;

public class TweetDetailViewModel extends TweetViewModel {

    public TweetDetailViewModel(Context context, Tweet tweet) {

        super(context, tweet);
    }

    public void retweet(long id, ImageView ivRetweetIcon) {

        client.retweet(id, new RetweetCallback() {
            @Override
            public void onRetweet(Tweet newTweet) {

                tweet = newTweet;
                setRetweetIcon(true);

                ivRetweetIcon.setImageDrawable(retweetIcon);
            }

            @Override
            public void onRetweetFailed() {

                setRetweetIcon(false);
            }
        });
    }

    public void favorite(long id, ImageView ivFavoriteIcon) {

        FavoriteCallback callback = new FavoriteCallback() {
            @Override
            public void onFavorite(Tweet newTweet) {

                tweet = newTweet;
                setFavoriteIcon(true);

                // API occasionally incorrectly returns 0 for favorite count after POST
                if (getFavoriteCount().equals("")) {
                    tweet.favoriteCount = "1";
                }

                ivFavoriteIcon.setImageDrawable(favoriteIcon);
            }

            @Override
            public void onFavoriteFailed() {

            }
        };

        FavoriteRequest.builder()
                .apiUrl(FAVORITES_CREATE_ENDPOINT)
                .id(id)
                .callback(callback)
                .build()
                .execute();
    }

    public void unfavorite(long id, ImageView ivFavoriteIcon) {

        FavoriteCallback callback = new FavoriteCallback() {
            @Override
            public void onFavorite(Tweet newTweet) {

                tweet = newTweet;
                setFavoriteIcon(false);

                ivFavoriteIcon.setImageDrawable(favoriteIcon);
            }

            @Override
            public void onFavoriteFailed() {

            }
        };

        FavoriteRequest.builder()
                .apiUrl(FAVORITES_DESTROY_ENDPOINT)
                .id(id)
                .callback(callback)
                .build()
                .execute();
    }
}
