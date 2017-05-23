package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.deonna.twitterclient.events.DirectMessagesCallback;
import com.deonna.twitterclient.models.DirectMessage;

import java.util.List;

public class DirectMessagesReceivedViewModel extends DirectMessagesListViewModel {

    public DirectMessagesReceivedViewModel(Context context, FragmentManager fragmentManager) {
        super(context, fragmentManager);
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
