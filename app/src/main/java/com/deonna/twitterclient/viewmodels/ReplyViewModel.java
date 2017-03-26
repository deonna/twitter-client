package com.deonna.twitterclient.viewmodels;

import android.content.Context;

import com.deonna.twitterclient.models.User;

public class ReplyViewModel extends ComposeViewModel {

    public ReplyViewModel(Context context, User user) {
        super(context, user);
    }

    public String getInReplyToString() {

        return "In reply to " + currentUser.name;
    }

    public String getScreenName() {

        return "@" + currentUser.screenName + " ";
    }
}
