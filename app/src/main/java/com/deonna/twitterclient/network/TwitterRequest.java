package com.deonna.twitterclient.network;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public interface TwitterRequest {

    String getPath();
    RequestParams getParams();
    JsonHttpResponseHandler getHandler();
}
