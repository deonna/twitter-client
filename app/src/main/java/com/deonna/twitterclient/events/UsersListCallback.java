package com.deonna.twitterclient.events.callbacks;

import com.deonna.twitterclient.models.User;

import java.util.List;

public interface UsersListCallback {

    void onUsersReceived(List<User> newUsers, Long nextCursor);
    void onUsersReceivedError();
}
