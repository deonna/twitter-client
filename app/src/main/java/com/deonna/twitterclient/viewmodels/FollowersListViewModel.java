package com.deonna.twitterclient.viewmodels;

import android.content.Context;

import com.deonna.twitterclient.callbacks.UsersListCallback;
import com.deonna.twitterclient.models.User;

import java.util.List;

public class FollowersListViewModel extends UsersListViewModel {

    public FollowersListViewModel(Context context, User user) {

        super(context, user);
    }

    @Override
    public void onCreate() {


    }

    private void getFollowersList() {

//        client.getFollowersList(get ,new UsersListCallback() {
//
//            @Override
//            public void onUsersReceived(List<User> newUsers) {
//
//            }
//
//            @Override
//            public void onUsersReceivedError() {
//
//            }
//        });
    }
}
