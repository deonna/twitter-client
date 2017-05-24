package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.network.requests.FollowRequest;
import com.deonna.twitterclient.views.adapters.UsersListAdapter;
import com.deonna.twitterclient.events.FollowCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

import static com.deonna.twitterclient.network.requests.FollowRequest.FOLLOW_PATH;
import static com.deonna.twitterclient.network.requests.FollowRequest.UNFOLLOW_PATH;

public class UserViewModel {

    protected final Context context;
    protected User user;

    protected Drawable isFollowingIcon;

    protected UsersListAdapter usersListAdapter;
    protected TwitterOauthClient client;

    public UserViewModel(Context context, User user, UsersListAdapter adapter) {

        this.context = context;
        this.user = user;

        setIsFollowingIcon(user.isFollowing);

        client = TwitterApplication.getRestClient();

        usersListAdapter = adapter;
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

    public Drawable getIsFollowingIcon() {

        return isFollowingIcon;
    }

    public void setIsFollowingIcon(boolean isFollowing) {

        if (isFollowing) {
            isFollowingIcon = context.getDrawable(R.drawable.ic_person_followed);
        } else {
            isFollowingIcon = context.getDrawable(R.drawable.ic_person_not_followed);
        }
    }

    public void unfollow(String screenName, ImageView ivIsFollowingIcon) {

        FollowCallback callback = new FollowCallback() {


            @Override
            public void onFollow(User unfollowedUser) {

                user = unfollowedUser;
                setIsFollowingIcon(false);

                ivIsFollowingIcon.setImageDrawable(getIsFollowingIcon());
            }

            @Override
            public void onFollowFailed() {

            }
        };

        FollowRequest.builder()
                .apiUrl(UNFOLLOW_PATH)
                .screenName(screenName)
                .callback(callback)
                .build()
                .execute();
    }

    public void follow(String screenName, ImageView ivIsFollowingIcon) {

        FollowCallback callback = new FollowCallback() {

            @Override
            public void onFollow(User followedUser) {

                user = followedUser;
                setIsFollowingIcon(true);

                ivIsFollowingIcon.setImageDrawable(getIsFollowingIcon());
            }

            @Override
            public void onFollowFailed() {

            }
        };

        FollowRequest.builder()
                .apiUrl(FOLLOW_PATH)
                .screenName(screenName)
                .follow(true)
                .callback(callback)
                .build()
                .execute();
    }
}
