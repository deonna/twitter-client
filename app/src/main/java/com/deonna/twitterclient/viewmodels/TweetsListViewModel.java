package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.callbacks.TweetsReceivedCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.util.ArrayList;
import java.util.List;

public class TweetsListViewModel implements ViewModel {

    protected static final String TAG = TweetsListViewModel.class.getSimpleName();
    protected static final String ERROR = "Error loading Tweets!";

    protected final Context context;

    protected final List<Tweet> tweets;
    protected final TweetsAdapter tweetsAdapter;

    protected final TwitterOauthClient client;

    protected Long maxId;

    public TweetsListViewModel(Context context, FragmentManager fragmentManager) {

        this.context = context;

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(context, tweets, fragmentManager);

        client = TwitterApplication.getRestClient();
    }

    @Override
    public void onCreate() {

//        getHomeTimeline();
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

    public EndlessRecyclerViewScrollListener initializeEndlessScrollListener(LinearLayoutManager layoutManager) {

        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                getNextOldestTweets();
            }
        };
    }

    protected void getNextOldestTweets() {

        client.getNextOldestTweets(maxId, new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);
            }

            @Override
            public void onTweetsReceivedError() {

            }
        });
    }

    protected Long getMaxIdForNextFetch(List<Tweet> tweets) {

        if (!tweets.isEmpty()) {
            //want to get the lowest number
            return tweets.get(tweets.size() - 1).id - 1;
        } else {
            return maxId - 1;
        }
    }

    public void getNewestTweets(final SwipeRefreshLayout srlTimeline) {

        Long sinceId = null;

        if (!tweets.isEmpty()) {
            sinceId = tweets.get(0).id - 1;
        } else {
            sinceId = 1L;
        }

        client.getNewestTweets(sinceId, new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(0, newTweets);
                tweetsAdapter.notifyItemRangeChanged(0, newTweets.size());

                srlTimeline.setRefreshing(false);
            }

            @Override
            public void onTweetsReceivedError() {

               srlTimeline.setRefreshing(false);
            }
        });
    }

    public void addNewlyComposedTweet() {

        Long sinceId = null;

        if (!tweets.isEmpty()) {
            sinceId = tweets.get(0).id - 1;
        } else {
            sinceId = 1L;
        }

        client.getNewestTweets(sinceId, new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(0, newTweets);
                tweetsAdapter.notifyItemRangeChanged(0, newTweets.size());
            }

            @Override
            public void onTweetsReceivedError() {

            }
        });
    }

    public void loadTweetsFromDb() {

        tweets.addAll(Tweet.getAll());
        tweetsAdapter.notifyDataSetChanged();
    }

    public void clearDb() {

        Tweet.deleteAll();
    }


}
