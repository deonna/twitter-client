package com.deonna.twitterclient.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.adapters.DirectMessagesAdapter;
import com.deonna.twitterclient.databinding.ActivityDirectMessagesBinding;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.viewmodels.DirectMessagesViewModel;

public class DirectMessagesActivity extends AppCompatActivity {

    private DirectMessagesViewModel directMessagesViewModel;
    private ActivityDirectMessagesBinding binding;
    private DirectMessagesAdapter directMessagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        directMessagesViewModel = new DirectMessagesViewModel(DirectMessagesActivity.this);

        binding = DataBindingUtil.setContentView(DirectMessagesActivity.this, R.layout.activity_direct_messages);
        binding.setDirectMessagesViewModel(directMessagesViewModel);

        directMessagesViewModel.onCreate();

        //TODO: setup toolbar

        setupMessagesView();
    }

    protected void setupMessagesView() {

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(DirectMessagesActivity.this, DividerItemDecoration.VERTICAL);
        binding.rvDirectMessages.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(DirectMessagesActivity.this);
        binding.rvDirectMessages.setLayoutManager(layoutManager);

        binding.rvDirectMessages.setAdapter(directMessagesViewModel.getAdapter());

//        EndlessRecyclerViewScrollListener scrollListener = directMessagesViewModel.initializeEndlessScrollListener(layoutManager);
//
//        binding.rvDirectMessages.addOnScrollListener(scrollListener);
    }
}
