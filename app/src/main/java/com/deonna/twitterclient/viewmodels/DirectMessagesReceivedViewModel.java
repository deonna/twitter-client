package com.deonna.twitterclient.viewmodels;

import android.content.Context;

import com.deonna.twitterclient.callbacks.DirectMessagesCallback;
import com.deonna.twitterclient.models.DirectMessage;

import java.util.List;

/**
 * Created by deonna on 4/2/17.
 */

public class DirectMessagesReceivedViewModel extends DirectMessagesListViewModel {

    public DirectMessagesReceivedViewModel(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {

        getDirectMessagesReceived();
    }

    public void getDirectMessagesReceived() {

        client.getDirectMessagesReceived(maxId, new DirectMessagesCallback() {

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
}
