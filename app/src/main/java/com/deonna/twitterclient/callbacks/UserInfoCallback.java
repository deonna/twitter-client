package com.deonna.twitterclient.callbacks;

import com.deonna.twitterclient.models.User;

public interface UserInfoCallback {

    void onUserInfoReceived(User user);
    void onUserInfoError();
}
