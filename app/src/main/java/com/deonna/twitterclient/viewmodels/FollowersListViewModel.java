package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.callbacks.UsersListCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;

import java.util.List;

public class FollowersListViewModel extends UsersListViewModel {

    public FollowersListViewModel(Context context, User user) {

        super(context, user);
    }

    @Override
    public void onCreate() {

        getFollowersList();
    }

    public void getFollowersList() {

        client.getFollowersList(profileUser.screenName, new UsersListCallback() {

            @Override
            public void onUsersReceived(List<User> newUsers) {

                users.addAll(newUsers);
                usersListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUsersReceivedError() {

            }
        });
    }

    public void getNextOldestFollowersList() {

//        client.getFollowersList(profileUser.screenName, new UsersListCallback() {
//
//            @Override
//            public void onUsersReceived(List<User> newUsers) {
//
//                users.addAll(newUsers);
//                usersListAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onUsersReceivedError() {
//
//            }
//        });
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
