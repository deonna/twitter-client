package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.view.View;

import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

public class UserViewModel {

    private final Context context;
    private final User user;

    public UserViewModel(Context context, User user) {

        this.context = context;
        this.user = user;
    }

    public String getName() {

        return user.name;
    }

    public String getScreenName() {

        return "@" + user.screenName;
    }

    public String getLargeProfileImageUrl() {

        return user.getLargeProfileImageUrl();
    }

    public int getIsVerifiedVisibility() {

        return user.isVerified ? View.VISIBLE : View.GONE;
    }
}
