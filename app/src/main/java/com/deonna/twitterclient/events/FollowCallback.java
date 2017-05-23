package com.deonna.twitterclient.events;

import com.deonna.twitterclient.models.User;

public interface FollowCallback {

    void onFollow(User user);
    void onFollowFailed();
}
