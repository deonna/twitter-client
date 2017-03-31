package com.deonna.twitterclient.callbacks;

import com.deonna.twitterclient.models.Tweet;

public interface FavoriteCallback {

    void onFavorite(Tweet newTweet);
    void onFavoriteFailed();
}
