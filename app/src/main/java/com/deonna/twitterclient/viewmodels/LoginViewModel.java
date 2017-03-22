package com.deonna.twitterclient.viewmodels;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.deonna.twitterclient.TwitterClient;
import com.deonna.twitterclient.models.User;

public class LoginViewModel {

    private Context context;

    public LoginViewModel(Context context) {

        this.context = context;
    }
}
