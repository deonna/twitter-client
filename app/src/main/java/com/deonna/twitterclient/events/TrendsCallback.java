package com.deonna.twitterclient.events;

import com.deonna.twitterclient.models.Trend;

import java.util.List;

public interface TrendsCallback {

    void onTrendsRecieved(List<Trend> trends);
    void onTrendsError();
}
