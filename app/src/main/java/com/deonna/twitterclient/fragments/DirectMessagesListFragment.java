package com.deonna.twitterclient.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.FragmentDirectMessagesListBinding;
import com.deonna.twitterclient.viewmodels.DirectMessagesListViewModel;

public class DirectMessagesListFragment extends Fragment {

    protected DirectMessagesListViewModel directMessagesListViewModel;
    protected FragmentDirectMessagesListBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        setupViewModel(new DirectMessagesListViewModel(getContext(), getFragmentManager()));
        setupBindings(inflater, parent);

        setupMessagesView();

        return binding.getRoot();
    }

    protected void setupViewModel(DirectMessagesListViewModel viewModel) {

        directMessagesListViewModel = viewModel;
        directMessagesListViewModel.onCreate();
    }

    protected void setupBindings(LayoutInflater inflater, @Nullable ViewGroup parent) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direct_messages_list, parent, false);

        binding.setDirectMessagesListViewModel(directMessagesListViewModel);
        binding.executePendingBindings();
    }

    protected void setupMessagesView() {

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.rvDirectMessages.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvDirectMessages.setLayoutManager(layoutManager);

        binding.rvDirectMessages.setAdapter(directMessagesListViewModel.getAdapter());

//        EndlessRecyclerViewScrollListener scrollListener = directMessagesListViewModel.initializeEndlessScrollListener(layoutManager);
//
//        binding.rvDirectMessages.addOnScrollListener(scrollListener);
    }
}
