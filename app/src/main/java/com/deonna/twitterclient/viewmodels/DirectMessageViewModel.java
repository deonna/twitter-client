package com.deonna.twitterclient.viewmodels;

import android.content.Context;

import com.deonna.twitterclient.models.DirectMessage;
import com.deonna.twitterclient.utilities.Times;

public class DirectMessageViewModel {

    private final Context context;
    private final DirectMessage directMessage;

    public DirectMessageViewModel(Context context, DirectMessage directMessage) {

        this.context = context;
        this.directMessage = directMessage;
    }

    public String getName() {

        return directMessage.sender.name;
    }

    public String getText() {

        return directMessage.text;
    }

    public String getRelativeTimestamp() {

        return Times.getRelativeTimestamp(directMessage.createdAt);
    }

    public String getProfileImageUrl() {

        return directMessage.sender.getLargeProfileImageUrl();
    }
}
