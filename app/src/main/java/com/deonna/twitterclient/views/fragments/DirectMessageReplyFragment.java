package com.deonna.twitterclient.views.fragments;

import android.os.Bundle;

import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.viewmodels.ReplyViewModel;

import org.parceler.Parcels;

public class DirectMessageReplyFragment extends ReplyFragment {

    public static DirectMessageReplyFragment newInstance(User currentUser) {

        DirectMessageReplyFragment directMessageReplyFragment = new DirectMessageReplyFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_CURRENT_USER, Parcels.wrap(currentUser));

        directMessageReplyFragment.setArguments(args);

        return directMessageReplyFragment;
    }

    @Override
    protected void setupClickEvents(ReplyViewModel replyViewModel) {

        binding.btSendTweet.setOnClickListener((view) -> {

            String directMessage = binding.etNewTweet.getText().toString();
            replyViewModel.sendDirectMessage(directMessage);

            dismiss();
        });
    }
}
