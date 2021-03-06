package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.deonna.twitterclient.views.adapters.DirectMessagesAdapter;
import com.deonna.twitterclient.models.DirectMessage;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.util.ArrayList;
import java.util.List;

public class DirectMessagesListViewModel implements ViewModel {

    protected final Context context;
    protected final TwitterOauthClient client;

    protected final List<DirectMessage> directMessages;
    protected final DirectMessagesAdapter adapter;

    protected Long maxId;

    public DirectMessagesListViewModel(Context context, FragmentManager fragmentManager) {

        this.context = context;

        directMessages = new ArrayList<>();
        adapter = new DirectMessagesAdapter(context, directMessages, fragmentManager);

        client = TwitterApplication.getRestClient();
    }

    @Override
    public void onCreate() {
    }

    public DirectMessagesAdapter getAdapter() {

        return adapter;
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
