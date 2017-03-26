package com.deonna.twitterclient.viewmodels;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.deonna.twitterclient.activities.ProfileActivity;
import com.deonna.twitterclient.adapters.UserTimelineAdapter;
import com.deonna.twitterclient.callbacks.TweetsCallback;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.network.TwitterOauthClient;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.TwitterApplication;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel implements ViewModel {

    private final TwitterOauthClient client;


    private ProfileActivity context;
    private User user;
    private List<Tweet> tweets;
//    private UserTimelineAdapter userTimelineAdapter;
    private Long maxId;

    public ProfileViewModel(ProfileActivity context, User user) {

        this.context = context;
        this.user = user;
//        this.tweets = new ArrayList<>();
//        this.userTimelineAdapter = new UserTimelineAdapter(this.context, tweets);

        client = TwitterApplication.getRestClient();

    }

    @Override
    public void onCreate() {

//        getUserTimeline();
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

    public String getName() {

        return user.name;
    }

    public int getIsVerifiedVisibility() {

        return user.isVerified ? View.VISIBLE : View.GONE;
    }

    public String getScreenName() {

        return  "@" + user.screenName;
    }

    public int getDescriptionVisibility() {

        return (user.description == null || user.description.isEmpty()) ? View.GONE : View.VISIBLE;
    }

    public String getDescription() {

        return user.description;
    }

    public int getLocationVisibility() {

        return (user.url == null || user.location.isEmpty()) ? View.GONE : View.VISIBLE;
    }

    public String getLocation() {

        return user.location;
    }

    public int getLinkVisibility() {

        return (user.url == null || user.url.isEmpty()) ? View.GONE : View.VISIBLE;
    }

    public String getUrl() {

        return user.url;
    }

    public String getNumFollowing() {

        return String.valueOf(user.numFollowing);
    }

    public String getNumFollowers() {

        return String.valueOf(user.numFollowers);
    }

    public String getLargeProfileImageUrl() {

        return user.getLargeProfileImageUrl();
    }

    public String getBackgroundImageUrl() {

        return user.getBackgroundImageUrl();
    }

//    private Long getMaxIdForNextFetch(List<Tweet> tweets) {
//
//        if (!tweets.isEmpty()) {
//            //want to get the lowest number
//            return tweets.get(tweets.size() - 1).id;
//        } else {
//
//            return maxId - 1;
//        }
//    }

//    public void getUserTimeline() {
//
//        client.getUserTimeline(user.screenName, new TweetsCallback() {
//
//            @Override
//            public void onTweetsReceived(List<Tweet> newTweets) {
//                tweets.addAll(newTweets);
//                userTimelineAdapter.notifyDataSetChanged();
//
//                maxId = getMaxIdForNextFetch(newTweets);
//            }
//
//            @Override
//            public void onTweetsError() {
//
//            }
//        });
//    }
//
//    private void getNextOldestTweets() {
//
//        client.getNextOldestUserTimelineTweets(user.screenName, maxId, new TweetsCallback() {
//
//            @Override
//            public void onTweetsReceived(List<Tweet> newTweets) {
//
//                tweets.addAll(newTweets);
//                userTimelineAdapter.notifyDataSetChanged();
//
//                maxId = getMaxIdForNextFetch(newTweets) - 1;
//            }
//
//            @Override
//            public void onTweetsError() {
//
//            }
//        });
//    }
//
//    public EndlessRecyclerViewScrollListener initializeEndlessScrollListener(LinearLayoutManager layoutManager) {
//
//        return new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//
//                getNextOldestTweets();
//            }
//        };
//    }
//
//
//    public RecyclerView.Adapter getAdapter() {
//
//        return userTimelineAdapter;
//    }
}
