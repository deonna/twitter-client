package com.deonna.twitterclient.views.activities;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.views.adapters.DirectMessagesAdapter;
import com.deonna.twitterclient.events.NewTweetsListener;
import com.deonna.twitterclient.databinding.ActivityDirectMessagesBinding;
import com.deonna.twitterclient.views.fragments.DirectMessagesListFragment;
import com.deonna.twitterclient.views.fragments.DirectMessagesReceivedFragment;
import com.deonna.twitterclient.views.fragments.DirectMessagesSentFragment;
import com.deonna.twitterclient.viewmodels.DirectMessagesViewModel;

import java.util.HashMap;
import java.util.Map;

public class DirectMessagesActivity extends AppCompatActivity implements NewTweetsListener {

    private DirectMessagesViewModel directMessagesViewModel;
    private ActivityDirectMessagesBinding binding;
    private DirectMessagesAdapter directMessagesAdapter;
    private DirectMessagesPagerAdapter directMessagesPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        directMessagesViewModel = new DirectMessagesViewModel(DirectMessagesActivity.this);

        binding = DataBindingUtil.setContentView(DirectMessagesActivity.this, R.layout.activity_direct_messages);
        binding.setDirectMessagesViewModel(directMessagesViewModel);

        setupTabs();
    }

    @Override
    public void displayNewestTweets() {

    }

    private void setupTabs() {

        directMessagesPagerAdapter = new DirectMessagesPagerAdapter(getSupportFragmentManager());

        binding.vpTimelines.setAdapter(directMessagesPagerAdapter);
        binding.pstsTimelines.setViewPager(binding.vpTimelines);
    }

    public class DirectMessagesPagerAdapter extends FragmentPagerAdapter {

        final int RECIEVED_POSITION = 0;
        final int SENT_POSITION = 1;

        final String[] tabTitles = { "Received", "Sent" };

        Map<Integer, DirectMessagesListFragment> positionToFragment;

        public DirectMessagesPagerAdapter(FragmentManager fm) {

            super(fm);

            positionToFragment = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case RECIEVED_POSITION:
                    DirectMessagesReceivedFragment messagesReceivedFragment = new DirectMessagesReceivedFragment();
                    positionToFragment.put(position, messagesReceivedFragment);

                    return messagesReceivedFragment;

                case SENT_POSITION:
                    DirectMessagesSentFragment messagesSentFragment = new DirectMessagesSentFragment();
                    positionToFragment.put(position, messagesSentFragment);

                    return messagesSentFragment;

                default:
                    DirectMessagesReceivedFragment defaultFragment = new DirectMessagesReceivedFragment();
                    positionToFragment.put(position, defaultFragment);

                    return defaultFragment;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public DirectMessagesListFragment getCurrentFragment(int position) {

            return positionToFragment.get(position);
        }
    }
}
