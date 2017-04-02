package com.deonna.twitterclient.viewmodels;

import android.content.Context;

import com.deonna.twitterclient.adapters.DirectMessagesAdapter;
import com.deonna.twitterclient.callbacks.DirectMessagesCallback;
import com.deonna.twitterclient.models.DirectMessage;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.util.ArrayList;
import java.util.List;

public class DirectMessagesViewModel implements ViewModel {

    private final Context context;
    private final TwitterOauthClient client;

    private final List<DirectMessage> directMessages;
    private final DirectMessagesAdapter adapter;

    private Long maxId;

    public DirectMessagesViewModel(Context context) {

        this.context = context;

        directMessages = new ArrayList<>();
        adapter = new DirectMessagesAdapter(context, directMessages);

        client = TwitterApplication.getRestClient();
    }

    @Override
    public void onCreate() {

        getDirectMessages();
    }

    public DirectMessagesAdapter getAdapter() {

        return adapter;
    }

    public void getDirectMessages() {

        client.getDirectMessages(maxId, new DirectMessagesCallback() {

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
