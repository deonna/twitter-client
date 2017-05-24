package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.events.UsersListCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.UsersListRequest;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;

import java.util.List;

import static com.deonna.twitterclient.network.UsersListRequest.SHOW_FOLLOWING_PATH;

public class FollowingListViewModel extends UsersListViewModel {

    private Long cursor;

    public FollowingListViewModel(Context context, User user) {

        super(context, user);
    }

    @Override
    public void onCreate() {

        getFollowingList();
    }

    public void getFollowingList() {

        getNextOldestFollowingList();
    }

    public void getNextOldestFollowingList() {

        UsersListCallback callback = new UsersListCallback() {

            @Override
            public void onUsersReceived(List<User> newUsers, Long nextCursor) {

                users.addAll(newUsers);
                usersListAdapter.notifyDataSetChanged();

                cursor = nextCursor;
            }

            @Override
            public void onUsersReceivedError() {

            }
        };

        UsersListRequest.builder()
                .apiUrl(SHOW_FOLLOWING_PATH)
                .cursor(cursor)
                .screenName(profileUser.screenName)
                .callback(callback)
                .build()
                .execute();
    }

    public EndlessRecyclerViewScrollListener initializeEndlessScrollListener(LinearLayoutManager layoutManager) {

        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                getNextOldestFollowingList();
            }
        };
    }
}



