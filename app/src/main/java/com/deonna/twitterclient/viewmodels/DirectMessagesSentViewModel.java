package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.deonna.twitterclient.events.DirectMessagesCallback;
import com.deonna.twitterclient.models.DirectMessage;
import com.deonna.twitterclient.network.DirectMessagesRequest;

import java.util.List;

import static com.deonna.twitterclient.network.DirectMessagesRequest.SENT_DIRECT_MESSAGES_PATH;

public class DirectMessagesSentViewModel extends DirectMessagesListViewModel {

    public DirectMessagesSentViewModel(Context context, FragmentManager fragmentManager) {

        super(context, fragmentManager);
    }

    @Override
    public void onCreate() {

        getDirectMessagesSent();
    }

    public void getDirectMessagesSent() {

        DirectMessagesCallback callback = new DirectMessagesCallback() {

            @Override
            public void onDirectMessagesReceived(List<DirectMessage> messages) {

                directMessages.addAll(messages);
                adapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(messages);
            }

            @Override
            public void onDirectMessagesError() {

            }
        };

        DirectMessagesRequest.builder()
                .apiUrl(SENT_DIRECT_MESSAGES_PATH)
                .maxId(maxId)
                .callback(callback)
                .build()
                .execute();
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
