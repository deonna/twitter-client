package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.view.View;

import com.deonna.twitterclient.models.User;

public class ProfileViewModel {

    private Context context;
    private User user;

    public ProfileViewModel(Context context, User user) {

        this.context = context;
        this.user = user;
    }

    public String getName() {

        return user.name;
    }

    public int getIsVerifiedVisibility() {

        return user.isVerified ? View.VISIBLE : View.GONE;
    }

    public String getScreenName() {

        return  "@" + user.screenName;
    }

    public String getDescription() {

        return user.description;
    }

    public int getLocationVisibility() {

        return user.location.isEmpty() ? View.GONE : View.VISIBLE;
    }

    public String getLocation() {

        return user.location;
    }

    public int getLinkVisibility() {

        return user.url.isEmpty() ? View.GONE : View.VISIBLE;
    }

    public String getUrl() {

        return user.url;
    }

    public String getNumFollowing() {

        return String.valueOf(user.numFollowing);
    }

    public String getNumFollowers() {

        return String.valueOf(user.numFollowers);
    }

    public String getLargeProfileImageUrl() {

        return user.getLargeProfileImageUrl();
    }

    public String getBackgroundImageUrl() {

        return user.getBackgroundImageUrl();
    }
}
