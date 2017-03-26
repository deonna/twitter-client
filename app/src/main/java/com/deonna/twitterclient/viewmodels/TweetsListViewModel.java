package com.deonna.twitterclient.viewmodels;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.util.ArrayList;
import java.util.List;

public class TweetsListViewModel implements ViewModel {

    private static final String TAG = TimelineViewModel.class.getSimpleName();
    private static final String ERROR = "Error loading Tweets!";

    private final Context context;

    private final List<Tweet> tweets;
    private final TweetsAdapter tweetsAdapter;

    private final TwitterOauthClient client;

    private Long maxId;

    public TweetsListViewModel(Context context, FragmentManager fragmentManager) {

        this.context = context;

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(context, tweets, fragmentManager);

        client = TwitterApplication.getRestClient();
    }

    public TweetsAdapter getAdapter() {

        return tweetsAdapter;
    }

    public EndlessRecyclerViewScrollListener initializeEndlessScrollListener(LinearLayoutManager layoutManager) {

        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                getNextOldestTweets();
            }
        };
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

    private Long getMaxIdForNextFetch(List<Tweet> tweets) {

        if (!tweets.isEmpty()) {
            //want to get the lowest number
            return tweets.get(tweets.size() - 1).id;
        }

        return null;
    }


    @Override
    public void onCreate() {

        getHomeTimeline();
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
}
