package com.deonna.twitterclient.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ItemUserBinding;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.UserViewModel;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<User> users;

    private Context context;

    public UsersListAdapter(Context context, List<User> users) {

        this.context = context;
        this.users = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemUserBinding userBinding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_user,
                        parent,
                        false
                );

        return new UsersListViewHolder(userBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        User user = users.get(position);

        UsersListViewHolder usersListViewHolder = (UsersListViewHolder) holder;
        ItemUserBinding userBinding = usersListViewHolder.binding;
        UserViewModel userViewModel = new UserViewModel(context, user);

        userBinding.setUserViewModel(userViewModel);
        userBinding.executePendingBindings();

        userBinding.tvName.setText(userViewModel.getName());
        userBinding.tvScreenName.setText(userViewModel.getScreenName());
        loadProfileImage(userViewModel.getLargeProfileImageUrl(), userBinding.ivProfileImage, 10);
    }

    private void loadProfileImage(String url, ImageView ivProfileImage, int size) {

        Images.loadFromUrl(context, ivProfileImage, url, size, size);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UsersListViewHolder extends RecyclerView.ViewHolder {

        final ItemUserBinding binding;

        public UsersListViewHolder(ItemUserBinding itemView) {

            super(itemView.getRoot());

            binding = itemView;
        }
    }
}