package com.deonna.twitterclient.events.callbacks;

import com.deonna.twitterclient.models.User;

/**
 * Created by deonna on 4/2/17.
 */

public interface FollowCallback {

    void onFollow(User user);
    void onFollowFailed();
}
