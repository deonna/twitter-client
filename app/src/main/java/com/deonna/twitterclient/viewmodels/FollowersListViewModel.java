package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.events.callbacks.UsersListCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;

import java.util.List;

public class FollowersListViewModel extends UsersListViewModel {

    private Long cursor;

    public FollowersListViewModel(Context context, User user) {

        super(context, user);
    }

    @Override
    public void onCreate() {

        getFollowersList();
    }

    public void getFollowersList() {

        client.getFollowersList(profileUser.screenName, cursor, new UsersListCallback() {

            @Override
            public void onUsersReceived(List<User> newUsers, Long nextCursor) {

                users.addAll(newUsers);
                usersListAdapter.notifyDataSetChanged();

                cursor = nextCursor;
            }

            @Override
            public void onUsersReceivedError() {

            }
        });
    }

    public void getNextOldestFollowersList() {

        client.getFollowersList(profileUser.screenName, cursor, new UsersListCallback() {

            @Override
            public void onUsersReceived(List<User> newUsers, Long nextCursor) {

                users.addAll(newUsers);
                usersListAdapter.notifyDataSetChanged();

                cursor = nextCursor;
            }

            @Override
            public void onUsersReceivedError() {

            }
        });
    }

    public EndlessRecyclerViewScrollListener initializeEndlessScrollListener(LinearLayoutManager layoutManager) {

        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                getNextOldestFollowersList();
            }
        };
    }
}
