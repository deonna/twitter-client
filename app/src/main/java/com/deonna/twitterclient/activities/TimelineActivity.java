package com.deonna.twitterclient.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.adapters.TweetsAdapter;
//import com.deonna.twitterclient.databinding.ActivityTimelineBinding;
import com.deonna.twitterclient.databinding.ActivityTimelineBinding;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.viewmodels.TimelineViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TimelineActivity extends AppCompatActivity {

    @BindView(R.id.rvTimeline) RecyclerView rvTimeline;


    private TimelineViewModel timelineViewModel;

    private TweetsAdapter tweetsAdapter;
    private List<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timelineViewModel = new TimelineViewModel(TimelineActivity.this);

        ActivityTimelineBinding binding = DataBindingUtil.setContentView(TimelineActivity.this, R.layout.activity_timeline);
        binding.setTimelineViewModel(timelineViewModel);

        binding.rvTimeline.setLayoutManager(new LinearLayoutManager(TimelineActivity.this));
        binding.rvTimeline.setAdapter(timelineViewModel.getAdapter());

        timelineViewModel.onCreate();
//
//        setContentView(R.layout.activity_timeline);
//
//        ButterKnife.bind(this);
//
//        tweets = new ArrayList<>();
//        tweetsAdapter = new TweetsAdapter(TimelineActivity.this, tweets);
//
//        rvTimeline.setAdapter(tweetsAdapter);
//        rvTimeline.setLayoutManager(new LinearLayoutManager(TimelineActivity.this));
//
//        timelineViewModel = new TimelineViewModel(TimelineActivity.this);
//        timelineViewModel.getHomeTimeline();
    }
}
