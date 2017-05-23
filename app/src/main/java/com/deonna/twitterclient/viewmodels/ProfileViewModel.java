package com.deonna.twitterclient.viewmodels;

import android.view.View;

import com.deonna.twitterclient.views.activities.ProfileActivity;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

public class ProfileViewModel implements ViewModel {

    private final TwitterOauthClient client;

    private ProfileActivity context;
    private User user;

    public ProfileViewModel(ProfileActivity context, User user) {

        this.context = context;
        this.user = user;

        client = TwitterApplication.getRestClient();
    }

    @Override
    public void onCreate() {
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

    public int getDescriptionVisibility() {

        return (user.description == null || user.description.isEmpty()) ? View.GONE : View.VISIBLE;
    }

    public String getDescription() {

        return user.description;
    }

    public int getLocationVisibility() {

        return (user.url == null || user.location.isEmpty()) ? View.GONE : View.VISIBLE;
    }

    public String getLocation() {

        return user.location;
    }

    public int getUrlVisibility() {

        return (user.url == null || user.url.isEmpty()) ? View.GONE : View.VISIBLE;
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

    public String getBannerImageUrl() {

        return user.getBannerImageUrl();
    }

    public int getBannerImageVisibility() {

        return (user.profileBannerUrl == null || user.profileBannerUrl.isEmpty()) ? View.GONE : View.VISIBLE;
    }
}
