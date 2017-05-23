package com.deonna.twitterclient.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deonna.twitterclient.views.activities.SearchResultsActivity;
import com.deonna.twitterclient.viewmodels.SearchResultsViewModel;


public class SearchResultsFragment extends TweetsListFragment {

    private SearchResultsViewModel searchResultsViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        searchResultsViewModel = new SearchResultsViewModel(getContext(), getFragmentManager(), this);

        super.setupViewModel(searchResultsViewModel);
        super.setupBindings(inflater, parent);
        super.setupTimelineView();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        String searchTerm = getActivity().getIntent().getStringExtra(SearchResultsActivity.KEY_SEARCH_TERM);

        searchResultsViewModel.setSearchTerm(searchTerm);
        searchResultsViewModel.getSearchResults();
    }
}
