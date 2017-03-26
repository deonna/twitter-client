package com.deonna.twitterclient.viewmodels;

import android.content.Context;

import com.deonna.twitterclient.models.User;

public class ProfileViewModel {

    private Context context;
    private User user;

    public ProfileViewModel(Context context, User user) {

        this.context = context;
        this.user = user;
    }
}
