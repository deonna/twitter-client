package com.deonna.twitterclient.views.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ItemUserBinding;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Fonts;
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
        UserViewModel userViewModel = new UserViewModel(context, user, this);

        userBinding.setUserViewModel(userViewModel);
        userBinding.executePendingBindings();

        inititalizeTextViews(userBinding, userViewModel);

        setupIsFollowingClickListener(userBinding, userViewModel, user, position);
        setupFonts(userBinding);

        loadProfileImage(userViewModel.getLargeProfileImageUrl(), userBinding.ivProfileImage);
    }

    private void setupIsFollowingClickListener(ItemUserBinding userBinding, UserViewModel userViewModel, User user, int position) {

        userBinding.ivIsFollowing.setOnClickListener((view) -> {
            if (user.isFollowing) {
                userViewModel.unfollow(user.screenName, userBinding.ivIsFollowing);
                user.isFollowing = false;
            } else {
                userViewModel.follow(user.screenName, userBinding.ivIsFollowing);
                user.isFollowing = true;
            }
        });
    }

    private void loadProfileImage(String url, ImageView ivProfileImage) {

        Images.loadFromUrlWithFixedSize(context, ivProfileImage, url);
    }

    private void inititalizeTextViews(ItemUserBinding userBinding, UserViewModel userViewModel) {

        userBinding.tvName.setText(userViewModel.getName());
        userBinding.tvScreenName.setText(userViewModel.getScreenName());
    }

    private void setupFonts(ItemUserBinding userBinding) {

        userBinding.tvName.setTypeface(Fonts.fontBold);
        userBinding.tvScreenName.setTypeface(Fonts.fontRegular);
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