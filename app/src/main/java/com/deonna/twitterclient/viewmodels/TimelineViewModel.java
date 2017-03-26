package com.deonna.twitterclient.viewmodels;

import android.support.v4.app.FragmentManager;

import com.deonna.twitterclient.activities.TimelineActivity;
import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.callbacks.TweetsRefreshListener;
import com.deonna.twitterclient.callbacks.UserInfoCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.util.ArrayList;
import java.util.List;


public class TimelineViewModel implements ViewModel {

    private static final String TAG = TimelineViewModel.class.getSimpleName();
    private static final String ERROR = "Error loading Tweets!";

    private final TimelineActivity context;

    private final List<Tweet> tweets;
    private final TweetsAdapter tweetsAdapter;

    private final TwitterOauthClient client;

    private User currentUser;
    private Long maxId;

    private TweetsRefreshListener refreshListener;

    public TimelineViewModel(TimelineActivity context, FragmentManager fragmentManager) {

        this.context = context;

        refreshListener = (TweetsRefreshListener) context;

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(context, tweets, fragmentManager);

        client = TwitterApplication.getRestClient();

    }

    @Override
    public void onCreate() {

        getLoggedInUserInfo();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public TweetsAdapter getAdapter() {

        return tweetsAdapter;
    }

    public User getCurrentUser() {

        return currentUser;
    }

    private void getLoggedInUserInfo() {

        client.getLoggedInUserInfo(new UserInfoCallback() {
            @Override
            public void onUserInfoReceived(User user) {

                TwitterApplication.setCurrentUser(user);
                currentUser = user;
                context.loadCurrentUserProfileImage(currentUser.getLargeProfileImageUrl());
            }

            @Override
            public void onUserInfoError() {

            }
        });
    }

    public void getNewestTweets() {

        Long sinceId = null;

        if (!tweets.isEmpty()) {
            sinceId = tweets.get(0).id - 1;
        } else {
            sinceId = 1L;
        }

        client.getNewestTweets(sinceId, new TweetsCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(0, newTweets);
                tweetsAdapter.notifyItemRangeChanged(0, newTweets.size());

                refreshListener.finishRefreshing();
            }

            @Override
            public void onTweetsError() {

                refreshListener.finishRefreshing();
            }
        });
    }
}
