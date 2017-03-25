package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.deonna.twitterclient.activities.TimelineActivity;
import com.deonna.twitterclient.callbacks.TweetsRefreshListener;
import com.deonna.twitterclient.callbacks.UserInfoCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public TimelineViewModel(TimelineActivity context) {

        this.context = context;

        refreshListener = (TweetsRefreshListener) context;

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(context, tweets);

        client = TwitterApplication.getRestClient();

    }

    @Override
    public void onCreate() {

        getHomeTimeline();
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

    public EndlessRecyclerViewScrollListener initializeEndlessScrollListener(LinearLayoutManager layoutManager) {

        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //TODO: Load and properly paginate new articles

                getNextOldestTweets();
            }
        };
    }

    public TweetsAdapter getAdapter() {

        return tweetsAdapter;
    }

    public void getHomeTimeline() {

        client.getHomeTimeline(new TweetsCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {
                Log.d(TAG, newTweets.toString());
                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsError() {

            }
        });
    }


    public User getCurrentUser() {

        return currentUser;
    }

    private void getNextOldestTweets() {

        client.getNextOldestTweets(maxId, new TweetsCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsError() {

            }
        });
    }

    private void getLoggedInUserInfo() {

        client.getLoggedInUserInfo(new UserInfoCallback() {
            @Override
            public void onUserInfoReceived(User user) {

                Log.d(TAG, user.toString());
                currentUser = user;
            }

            @Override
            public void onUserInfoError() {

            }
        });
    }

    private Long getMaxIdForNextFetch(List<Tweet> tweets) {

        if (!tweets.isEmpty()) {
            //want to get the lowest number
            return tweets.get(tweets.size() - 1).id;
        }

        return null;
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
