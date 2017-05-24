package com.deonna.twitterclient.viewmodels;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.network.TimelineRequest;
import com.deonna.twitterclient.views.adapters.TweetsAdapter;
import com.deonna.twitterclient.events.TweetsReceivedCallback;
import com.deonna.twitterclient.views.fragments.TweetsListFragment;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.util.ArrayList;
import java.util.List;

import static com.deonna.twitterclient.network.TimelineRequest.HOME_TIMELINE_PATH;

public class TweetsTimelineViewModel implements ViewModel {

    protected static final String TAG = TweetsTimelineViewModel.class.getSimpleName();
    protected static final String ERROR = "Error loading Tweets!";

    protected final Context context;

    protected final List<Tweet> tweets;
    protected final TweetsAdapter tweetsAdapter;

    protected final TwitterOauthClient client;

    protected Long maxId = Long.MAX_VALUE;

    protected TweetsListFragment fragment;

    public TweetsTimelineViewModel(Context context, FragmentManager fragmentManager, TweetsListFragment fragment) {

        this.context = context;
        this.fragment = fragment;

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(context, tweets, fragmentManager);

        client = TwitterApplication.getRestClient();
    }

    @Override
    public void onCreate() {

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

        fragment.showProgressBar();

        TweetsReceivedCallback callback = new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();

                maxId = getMaxIdForNextFetch(newTweets);

                fragment.hideProgressBar();
            }

            @Override
            public void onTweetsReceivedError() {

                fragment.hideProgressBar();
            }
        };

        TimelineRequest.builder()
                .apiUrl(HOME_TIMELINE_PATH)
                .maxId(maxId)
                .callback(callback)
                .build()
                .execute();
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

        TweetsReceivedCallback callback = new TweetsReceivedCallback() {

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
        };

        TimelineRequest.builder()
                .apiUrl(HOME_TIMELINE_PATH)
                .sinceId(sinceId)
                .callback(callback)
                .build()
                .execute();
    }

    public void addNewlyComposedTweet() {

        Long sinceId = null;

        if (!tweets.isEmpty()) {
            sinceId = tweets.get(0).id - 1;
        } else {
            sinceId = 1L;
        }

        TweetsReceivedCallback callback = new TweetsReceivedCallback() {

            @Override
            public void onTweetsReceived(List<Tweet> newTweets) {

                tweets.addAll(0, newTweets);
                tweetsAdapter.notifyItemRangeChanged(0, newTweets.size());
            }

            @Override
            public void onTweetsReceivedError() {

            }
        };

        TimelineRequest.builder()
                .apiUrl(HOME_TIMELINE_PATH)
                .sinceId(sinceId)
                .callback(callback)
                .build()
                .execute();
    }

    public void loadTweetsFromDb() {

        tweets.addAll(Tweet.getAll());
        tweetsAdapter.notifyDataSetChanged();
    }

    public void clearDb() {

        Tweet.deleteAll();
    }
}
