package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.views.adapters.UsersListAdapter;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.util.ArrayList;
import java.util.List;

public class UsersListViewModel implements ViewModel {

    protected Context context;
    protected final User profileUser;
    protected final List<User> users;
    protected UsersListAdapter usersListAdapter;

    protected final TwitterOauthClient client;

    public UsersListViewModel(Context context, User user) {

        this.context = context;

        profileUser = user;

        users = new ArrayList<>();
        usersListAdapter = new UsersListAdapter(context, users);

        client = TwitterApplication.getRestClient();
    }

    @Override
    public void onCreate() {
    }

    public UsersListAdapter getAdapter() {

        return usersListAdapter;
    }

    public EndlessRecyclerViewScrollListener initializeEndlessScrollListener(LinearLayoutManager layoutManager) {

        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        };
    }
}
