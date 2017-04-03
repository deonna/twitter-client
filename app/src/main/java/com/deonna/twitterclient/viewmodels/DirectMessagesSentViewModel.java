package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.deonna.twitterclient.callbacks.DirectMessagesCallback;
import com.deonna.twitterclient.models.DirectMessage;

import java.util.List;

/**
 * Created by deonna on 4/2/17.
 */

public class DirectMessagesSentViewModel extends DirectMessagesListViewModel {

    public DirectMessagesSentViewModel(Context context, FragmentManager fragmentManager) {

        super(context, fragmentManager);
    }

    @Override
    public void onCreate() {

        getDirectMessagesSent();
    }

    public void getDirectMessagesSent() {

        client.getDirectMessagesSent(maxId, new DirectMessagesCallback() {

            @Override
            public void onDirectMessagesReceived(List<DirectMessage> messages) {

                directMessages.addAll(messages);
                adapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(messages);
            }

            @Override
            public void onDirectMessagesError() {

            }
        });
    }

    protected Long getMaxIdForNextFetch(List<DirectMessage> directMessages) {

        if (!directMessages.isEmpty()) {
            //want to get the lowest number
            return directMessages.get(directMessages.size() - 1).id - 1;
        } else {
            return maxId - 1;
        }
    }
}
