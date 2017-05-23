package com.deonna.twitterclient.viewmodels;

import com.deonna.twitterclient.events.DirectMessageSentCallback;
import com.deonna.twitterclient.events.NewTweetsListener;
import com.deonna.twitterclient.models.User;

public class ReplyViewModel extends ComposeViewModel {

    public ReplyViewModel(NewTweetsListener context, User user) {
        super(context, user);
    }

    public String getInReplyToString() {

        return "In reply to " + currentUser.name;
    }

    public String getScreenName() {

        return "@" + currentUser.screenName + " ";
    }

    public String getCharacterCount() {

        return String.valueOf(ComposeViewModel.INITIAL_CHARACTER_COUNT - getScreenName().length());
    }

    public void sendDirectMessage(String text) {

        client.sendDirectMessage(currentUser.screenName, text, new DirectMessageSentCallback() {

            @Override
            public void onDirectMessageSent() {

            }

            @Override
            public void onDirectMessageSentFailed() {

            }
        });
    }
}
