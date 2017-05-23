package com.deonna.twitterclient.events.callbacks;

import com.deonna.twitterclient.models.Trend;

import java.util.List;

/**
 * Created by deonna on 4/2/17.
 */

public interface TrendsCallback {

    void onTrendsRecieved(List<Trend> trends);
    void onTrendsError();
}
