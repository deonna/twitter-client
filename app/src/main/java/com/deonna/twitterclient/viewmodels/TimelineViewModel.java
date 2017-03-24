package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.deonna.twitterclient.callbacks.UserInfoCallback;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.TwitterApplication;
import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TimelineViewModel implements ViewModel {

    private static final String TAG = TimelineViewModel.class.getSimpleName();
    private static final String ERROR = "Error loading Tweets!";

    private final Context context;

    private final List<Tweet> tweets;
    private final TweetsAdapter tweetsAdapter;

    private final TwitterOauthClient client;

    private User currentUser;
    private Long maxId;

    public TimelineViewModel(Context context) {

        this.context = context;

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

                maxId = getMaxId(newTweets);
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
            public void onTweetsReceived(List<Tweet> tweets) {

                //TODO: Get and set max id based on tweets returned
                //TODO: Add new tweets
                //TODO: Notify adapter
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

    private Long getMaxId(List<Tweet> tweets) {

        Comparator idAscendingOrder = new Comparator<Tweet>() {

            @Override
            public int compare(Tweet tweet1, Tweet tweet2) {

                Long idDifference = tweet1.id - tweet2.id;
                if (idDifference > 1) {
                    return 1;
                } else if (idDifference < 1) {
                    return -1;
                }

                return 0;
            }
        };

        Tweet tweetWithMaxId = Collections.max(tweets, idAscendingOrder);

        return tweetWithMaxId.id;
    }
}
