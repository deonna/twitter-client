package com.deonna.twitterclient.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.deonna.twitterclient.views.fragments.ComposeFragment;
import com.deonna.twitterclient.utilities.TwitterApplication;

public class ReceiveDataActivity extends AppCompatActivity {

    private ComposeFragment composeFragment;
    private String title;
    private String url;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {

                // Make sure to check whether returned data will be null.
                title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
                url = intent.getStringExtra(Intent.EXTRA_TEXT);
                imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

                FragmentManager fragmentManager = getSupportFragmentManager();

                composeFragment = ComposeFragment.newInstance(TwitterApplication.getCurrentUser(), title, url);
                composeFragment.show(fragmentManager, ComposeFragment.LAYOUT_NAME);
            }
        }
    }
}
