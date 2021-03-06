package com.deonna.twitterclient.views.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.FragmentReplyBinding;
import com.deonna.twitterclient.events.NewTweetsListener;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.ReplyViewModel;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ReplyFragment extends DialogFragment {

    public static final String LAYOUT_NAME = "fragment_reply";
    protected static final String KEY_CURRENT_USER = "current_user";

    protected FragmentReplyBinding binding;

    public static ReplyFragment newInstance(User currentUser) {

        ReplyFragment replyFragment = new ReplyFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_CURRENT_USER, Parcels.wrap(currentUser));

        replyFragment.setArguments(args);

        return replyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        User currentUser = Parcels.unwrap(getArguments().getParcelable(KEY_CURRENT_USER));

        // Setup bindings
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reply, container, false);

        View fragmentView = binding.getRoot();

        ReplyViewModel replyViewModel = new ReplyViewModel((NewTweetsListener) getActivity(), currentUser);
        binding.setReplyViewModel(replyViewModel);

        // Add content
        setupFonts();
        setProfilePicture(
                currentUser.getLargeProfileImageUrl(),
                binding.ivUserProfile,
                ReplyViewModel.getImageSize()
        );
        setupClickEvents(replyViewModel);

        binding.executePendingBindings();

        int selectionPosition = replyViewModel.getScreenName().length() ;
        binding.etNewTweet.setSelection(selectionPosition);

        return fragmentView;
    }

    @Override
    public void onResume() {

        int height = getResources().getDimensionPixelSize(R.dimen.dialog_compose_height);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);

        super.onResume();
    }

    protected void setProfilePicture(String url, ImageView ivProfileImage, int size) {

        Glide.with(getActivity())
                .load(url)
                .override(size, size)
                .bitmapTransform(new RoundedCornersTransformation(getActivity(), 10, 2))
                .into(ivProfileImage);
    }

    protected void setupFonts() {

        binding.etNewTweet.setTypeface(Fonts.fontRegular);
        binding.btSendTweet.setTypeface(Fonts.fontExtraBold);
        binding.tvCharacterCount.setTypeface(Fonts.fontRegular);
    }

    protected void setupClickEvents(final ReplyViewModel replyViewModel) {

        binding.btSendTweet.setOnClickListener((view) -> {

            String newTweet = binding.etNewTweet.getText().toString();
            replyViewModel.sendNewTweet(newTweet);

            dismiss();
        });

        binding.ivCloseDialog.setOnClickListener((view) -> {

            dismiss();
        });

        final TextWatcher characterCountWatcher = getCharacterCountWatcher(replyViewModel);

        binding.etNewTweet.addTextChangedListener(characterCountWatcher);
    }

    protected TextWatcher getCharacterCountWatcher(final ReplyViewModel replyViewModel) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tvCharacterCount.setText(String.valueOf(replyViewModel.getCharacterCount(s.length())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
