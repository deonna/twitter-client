package com.deonna.twitterclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deonna.twitterclient.R;

public class SearchResultsActivity extends AppCompatActivity {

    public static final String KEY_SEARCH_TERM = "search_term";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
    }
}
