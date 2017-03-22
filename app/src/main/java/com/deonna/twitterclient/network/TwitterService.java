package com.deonna.twitterclient.network;

import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.models.TwitterResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TwitterService {

    String KEY_COUNT = "count";
    String KEY_SINCE_ID = "since_id";

    String REST_HOME_TIMELINE_URL = "/statuses/home_timeline.json";


    @GET(REST_HOME_TIMELINE_URL)
    Observable<List<Tweet>> getHomeTimeline(
            @Query(KEY_COUNT) String count,
            @Query(KEY_SINCE_ID) String sinceId
    );
}
