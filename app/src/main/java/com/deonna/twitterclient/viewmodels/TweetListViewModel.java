package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.deonna.twitterclient.activities.ProfileActivity;
import com.deonna.twitterclient.activities.SearchResultsActivity;
import com.deonna.twitterclient.activities.TimelineActivity;
import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.callbacks.FavoriteCallback;
import com.deonna.twitterclient.callbacks.RetweetCallback;
import com.deonna.twitterclient.callbacks.SearchResultsCallback;
import com.deonna.twitterclient.callbacks.UserInfoCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.models.User;

import org.parceler.Parcels;

import java.util.List;

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

                setRetweetIcon(true);

                ivRetweetIcon.setImageDrawable(retweetIcon);
                tvRetweetCount.setText(getRetweetCount());

                // add new retweet to timeline
                adapter.tweets.add(0, newTweet);
                adapter.notifyItemRangeChanged(0, adapter.tweets.size());

                // update RT count and icon of original tweet
                tweet.retweeted = true;
                tweet.retweetCount = newTweet.retweetCount;
                adapter.notifyItemChanged(position);

                ((TimelineActivity) context).scrollToTop();
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

                adapter.tweets.set(position, tweet);
                adapter.notifyItemChanged(position);
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

                adapter.tweets.set(position, tweet);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onFavoriteFailed() {

            }
        });
    }

    public void openProfileForUser(String screenName) {

        client.getUserInfo(screenName, new UserInfoCallback() {
            @Override
            public void onUserInfoReceived(User user) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ProfileActivity.KEY_USER, Parcels.wrap(user));

                context.startActivity(intent);
            }

            @Override
            public void onUserInfoError() {

            }
        });
    }

    public void getSearchResultsForHashtag(String hashtag) {

        Intent intent = new Intent(context, SearchResultsActivity.class);
        intent.putExtra(SearchResultsActivity.KEY_SEARCH_TERM, hashtag);

        context.startActivity(intent);

//        client.getSearchResultsForHashtag(hashtag, new SearchResultsCallback() {
//
//            @Override
//            public void onSearchResultsReceived(List<Tweet> results) {
//
//                Intent intent = new Intent(context, SearchResultsActivity.class);
//                intent.putExtra(SearchResultsActivity.KEY_RESULTS, Parcels.wrap(results));
//
//                context.startActivity(intent);
//            }
//
//            @Override
//            public void onSearchResultsError() {
//
//            }
//        });
    }
}
