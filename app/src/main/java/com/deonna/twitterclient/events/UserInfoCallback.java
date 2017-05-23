package com.deonna.twitterclient.events.callbacks;

import com.deonna.twitterclient.models.User;

public interface UserInfoCallback {

    void onUserInfoReceived(User user);
    void onUserInfoError();
}
