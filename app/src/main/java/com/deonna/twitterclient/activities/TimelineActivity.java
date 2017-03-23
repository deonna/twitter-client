package com.deonna.twitterclient.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ActivityTimelineBinding;
import com.deonna.twitterclient.viewmodels.TimelineViewModel;

public class TimelineActivity extends AppCompatActivity {

    private TimelineViewModel timelineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timelineViewModel = new TimelineViewModel(TimelineActivity.this);

        ActivityTimelineBinding binding = DataBindingUtil.setContentView(TimelineActivity.this, R.layout.activity_timeline);
        binding.setTimelineViewModel(timelineViewModel);

        binding.rvTimeline.setLayoutManager(new LinearLayoutManager(TimelineActivity.this));
        binding.rvTimeline.setAdapter(timelineViewModel.getAdapter());

        timelineViewModel.onCreate();
    }
}
