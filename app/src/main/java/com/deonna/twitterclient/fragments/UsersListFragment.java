package com.deonna.twitterclient.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.FragmentUsersListBinding;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.viewmodels.UsersListViewModel;

public class UsersListFragment extends DialogFragment {

    public static final String LAYOUT_NAME = "fragment_users_list";
    private static final String KEY_USERS_LIST = "users_list";

    protected UsersListViewModel usersListViewModel;
    private FragmentUsersListBinding binding;

    public static UsersListFragment newInstance() {

        UsersListFragment usersListFragment = new UsersListFragment();

        return usersListFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        usersListViewModel = new UsersListViewModel(getActivity());
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        usersListViewModel = new UsersListViewModel(getActivity());
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_list, container, false);

        View fragmentView = binding.getRoot();

        binding.setUsersListViewModel(usersListViewModel);
        binding.executePendingBindings();

        setupUsersListView();

        return fragmentView;
    }

    protected void setupUsersListView() {

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.rvUsers.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvUsers.setLayoutManager(layoutManager);

        binding.rvUsers.setAdapter(usersListViewModel.getAdapter());

        EndlessRecyclerViewScrollListener scrollListener = usersListViewModel.initializeEndlessScrollListener(layoutManager);

        binding.rvUsers.addOnScrollListener(scrollListener);
    }
}
