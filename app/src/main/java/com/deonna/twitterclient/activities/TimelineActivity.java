package com.deonna.twitterclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.TwitterApplication;
import com.deonna.twitterclient.adapters.TweetsAdapter;
import com.deonna.twitterclient.models.Tweet;
import com.deonna.twitterclient.network.TwitterApiClient;
import com.deonna.twitterclient.viewmodels.TimelineViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TimelineActivity extends AppCompatActivity {

    @BindView(R.id.rvTimeline) RecyclerView rvTimeline;

    private CompositeDisposable subscriptions;

    private TweetsAdapter tweetsAdapter;
    private TimelineViewModel timelineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ButterKnife.bind(this);

        tweetsAdapter = new TweetsAdapter(TimelineActivity.this);

        rvTimeline.setAdapter(tweetsAdapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(TimelineActivity.this));

        timelineViewModel = new TimelineViewModel(TimelineActivity.this, TwitterApiClient.getInstance());

        timelineViewModel.getTimeline();
//        subscriptions = new CompositeDisposable();
//
//        Disposable disposable = timelineViewModel
//                .getHomeTimeline()
//                .subscribe(new Observer<List<Tweet>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Tweet> value) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//        subscriptions.add(disposable);
    }
}
