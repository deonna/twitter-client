package com.deonna.twitterclient.views.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.FragmentUsersListBinding;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.EndlessRecyclerViewScrollListener;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.UsersListViewModel;

import org.parceler.Parcels;

public class UsersListFragment extends DialogFragment {

    public static final String LAYOUT_NAME = "fragment_users_list";
    protected static final String KEY_USER = "user";

    protected UsersListViewModel usersListViewModel;
    protected FragmentUsersListBinding binding;

    public static UsersListFragment newInstance(User user) {

        UsersListFragment usersListFragment = new UsersListFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_USER, Parcels.wrap(user));

        usersListFragment.setArguments(args);

        return usersListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        User user = getUser();

        setupViewModel(new UsersListViewModel(getActivity(), user));

        setupBindings(inflater, parent);
        setupUsersListView();

        return binding.getRoot();
    }

    protected void setupViewModel(UsersListViewModel viewModel) {

        usersListViewModel = viewModel;
        usersListViewModel.onCreate();
    }

    protected User getUser() {

        return (User) Parcels.unwrap(getArguments().getParcelable(KEY_USER));
    }

    protected void setupBindings(LayoutInflater inflater, @Nullable ViewGroup parent) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_list, parent, false);

        binding.setUsersListViewModel(usersListViewModel);
        binding.executePendingBindings();
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

    protected void addStylingToTitle(String title) {

        binding.tvTitle.setTypeface(Fonts.fontBold);
        binding.tvTitle.setGravity(Gravity.CENTER);
        binding.tvTitle.setText(title);
        binding.tvTitle.setPadding(0, 20, 0, 20);
    }
}
