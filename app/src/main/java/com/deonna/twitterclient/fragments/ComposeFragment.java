package com.deonna.twitterclient.fragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deonna.twitterclient.R;
import com.deonna.twitterclient.databinding.FragmentComposeBinding;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.ComposeViewModel;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ComposeFragment extends DialogFragment {

    public static final String LAYOUT_NAME = "fragment_compose";
    private static final String KEY_CURRENT_USER = "current_user";

    private FragmentComposeBinding binding;

    public static ComposeFragment newInstance(User currentUser) {

        ComposeFragment composeFragment = new ComposeFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_CURRENT_USER, Parcels.wrap(currentUser));

        composeFragment.setArguments(args);

        return composeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        User currentUser = (User) Parcels.unwrap(getArguments().getParcelable(KEY_CURRENT_USER));

        // Setup bindings
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compose, container, false);

        View fragmentView = binding.getRoot();

        ComposeViewModel composeViewModel = new ComposeViewModel(getActivity(), currentUser);
        binding.setComposeViewModel(composeViewModel);
        binding.executePendingBindings();

        // Add content
        setupFonts();
        setProfilePicture(
                currentUser.getLargeProfileImageUrl(),
                binding.ivUserProfile,
                ComposeViewModel.getImageSize()
        );
        setupClickEvents(composeViewModel);

        return fragmentView;
    }

    @Override
    public void onResume() {

        int height = getResources().getDimensionPixelSize(R.dimen.dialog_compose_height);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);

        super.onResume();
    }

    private void setProfilePicture(String url, ImageView ivProfileImage, int size) {

        Glide.with(getActivity())
                .load(url)
                .override(size, size)
                .bitmapTransform(new RoundedCornersTransformation(getActivity(), 10, 2))
                .into(ivProfileImage);
    }

    private void setupFonts() {

        binding.etNewTweet.setTypeface(Fonts.fontRegular);
        binding.btSendTweet.setTypeface(Fonts.fontExtraBold);
        binding.tvCharacterCount.setTypeface(Fonts.fontRegular);
    }

    private void setupClickEvents(final ComposeViewModel composeViewModel) {

        binding.btSendTweet.setOnClickListener((view) -> {

            String newTweet = binding.etNewTweet.getText().toString();
            composeViewModel.sendNewTweet(newTweet);

            dismiss();
        });

        binding.ivCloseDialog.setOnClickListener((view) -> {

            String newTweet = binding.etNewTweet.getText().toString();

            if (newTweet.isEmpty()) {
                dismiss();
            } else {
                //TODO: Prompt to save draft
                //Load this draft afterward
            }
        });

        final TextWatcher characterCountWatcher = getCharacterCountWatcher(composeViewModel);

        binding.etNewTweet.addTextChangedListener(characterCountWatcher);
    }

    private TextWatcher getCharacterCountWatcher(final ComposeViewModel composeViewModel) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tvCharacterCount.setText(String.valueOf(composeViewModel.getCharacterCount(s.length())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
