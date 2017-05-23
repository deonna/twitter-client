package com.deonna.twitterclient.events;

import com.deonna.twitterclient.models.DirectMessage;

import java.util.List;

public interface DirectMessagesCallback {

    void onDirectMessagesReceived(List<DirectMessage> messages);
    void onDirectMessagesError();
}
