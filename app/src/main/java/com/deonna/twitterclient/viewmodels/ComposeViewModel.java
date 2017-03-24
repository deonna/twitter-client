package com.deonna.twitterclient.viewmodels;

import com.deonna.twitterclient.models.User;

public class ComposeViewModel {

    private static final int IMAGE_SIZE = 36;

    private User currentUser;

    public ComposeViewModel(User user) {

        currentUser = user;
    }

    public static int getImageSize() {

        return IMAGE_SIZE;
    }
}
