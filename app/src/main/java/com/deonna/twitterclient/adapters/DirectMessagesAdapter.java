package com.deonna.twitterclient.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.ItemDirectMessageBinding;
import com.deonna.twitterclient.models.DirectMessage;
import com.deonna.twitterclient.utilities.Images;
import com.deonna.twitterclient.viewmodels.DirectMessageViewModel;

import java.util.List;

public class DirectMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<DirectMessage> directMessages;

    public DirectMessagesAdapter(Context context, List<DirectMessage> directMessages) {

        this.context = context;
        this.directMessages = directMessages;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemDirectMessageBinding directMessageBinding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_direct_message,
                        parent,
                        false
                );

        return new DirectMessageViewHolder(directMessageBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DirectMessage directMessage = directMessages.get(position);

        DirectMessageViewHolder viewHolder = (DirectMessageViewHolder) holder;
        ItemDirectMessageBinding binding = viewHolder.binding;
        DirectMessageViewModel viewModel = new DirectMessageViewModel(context, directMessage);

        binding.setDirectMessageViewModel(viewModel);
        binding.executePendingBindings();

        loadProfileImage(
            viewModel.getProfileImageUrl(),
            binding.ivProfileImage
        );
    }

    @Override
    public int getItemCount() {
        return directMessages.size();
    }

    public static class DirectMessageViewHolder extends RecyclerView.ViewHolder {

        final ItemDirectMessageBinding binding;

        public DirectMessageViewHolder(ItemDirectMessageBinding itemView) {

            super(itemView.getRoot());

            binding = itemView;
        }
    }

    private void loadProfileImage(String url, ImageView ivProfileImage) {

        Images.loadCircularImage(context, ivProfileImage, url);
    }
}
