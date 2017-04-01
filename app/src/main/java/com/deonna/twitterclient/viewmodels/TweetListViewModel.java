package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.callbacks.FavoriteCallback;
import com.deonna.twitterclient.callbacks.RetweetCallback;
import com.deonna.twitterclient.models.Tweet;

public class TweetListViewModel extends TweetViewModel {

    private TweetsAdapter adapter;

    public TweetListViewModel(Context context, Tweet tweet, TweetsAdapter adapter) {

        super(context, tweet);

        this.adapter = adapter;
    }

    public void retweet(long id, int position, ImageView ivRetweetIcon, TextView tvRetweetCount) {

        client.retweet(id, new RetweetCallback() {
            @Override
            public void onRetweet(Tweet newTweet) {

                tweet = newTweet;
                setRetweetIcon(true);

                ivRetweetIcon.setImageDrawable(retweetIcon);
                tvRetweetCount.setText(getRetweetCount());

                if (adapter != null) {
                    adapter.tweets.set(position, tweet);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onRetweetFailed() {

                setRetweetIcon(false);
            }
        });
    }

    public void favorite(long id, int position, ImageView ivFavoriteIcon, TextView tvFavoriteCount) {

        client.favoriteTweet(id, new FavoriteCallback() {
            @Override
            public void onFavorite(Tweet newTweet) {

                tweet = newTweet;
                setFavoriteIcon(true);

                // Sometimes have an issue with API incorrectly returning 0 for favorite count after POST

                if (getFavoriteCount().equals("")) {
                    tweet.favoriteCount = "1";
                }

                ivFavoriteIcon.setImageDrawable(favoriteIcon);
                tvFavoriteCount.setText(getFavoriteCount());

                if (adapter != null) {
                    adapter.tweets.set(position, tweet);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFavoriteFailed() {

            }
        });
    }

    public void unfavorite(long id, int position, ImageView ivFavoriteIcon, TextView tvFavoriteCount) {

        client.unfavoriteTweet(id, new FavoriteCallback() {
            @Override
            public void onFavorite(Tweet newTweet) {

                tweet = newTweet;
                setFavoriteIcon(false);

                ivFavoriteIcon.setImageDrawable(favoriteIcon);
                tvFavoriteCount.setText(getFavoriteCount());

                if (adapter != null) {
                    adapter.tweets.set(position, tweet);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFavoriteFailed() {

            }
        });
    }
}
