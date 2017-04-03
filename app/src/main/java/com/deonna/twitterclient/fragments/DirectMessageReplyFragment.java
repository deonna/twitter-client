package com.deonna.twitterclient.fragments;

import android.os.Bundle;

import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.viewmodels.ReplyViewModel;

import org.parceler.Parcels;

/**
 * Created by deonna on 4/2/17.
 */

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
