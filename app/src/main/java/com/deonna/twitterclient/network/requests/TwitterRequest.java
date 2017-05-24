package com.deonna.twitterclient.network.requests;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public interface TwitterRequest {

    String getPath();
    RequestParams getParams();
    JsonHttpResponseHandler getHandler();
}
