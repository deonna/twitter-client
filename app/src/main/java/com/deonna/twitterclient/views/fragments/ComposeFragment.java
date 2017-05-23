package com.deonna.twitterclient.views.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
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
import com.deonna.twitterclient.events.callbacks.NewTweetsListener;
import com.deonna.twitterclient.databinding.FragmentComposeBinding;
import com.deonna.twitterclient.models.Draft;
import com.deonna.twitterclient.models.User;
import com.deonna.twitterclient.utilities.Fonts;
import com.deonna.twitterclient.viewmodels.ComposeViewModel;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ComposeFragment extends DialogFragment {

    public static final String LAYOUT_NAME = "fragment_compose";
    private static final String KEY_CURRENT_USER = "current_user";
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";

    private FragmentComposeBinding binding;

    public static ComposeFragment newInstance(User currentUser) {

        ComposeFragment composeFragment = new ComposeFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_CURRENT_USER, Parcels.wrap(currentUser));
        args.putString(KEY_TITLE, "");
        args.putString(KEY_URL, "");

        composeFragment.setArguments(args);

        return composeFragment;
    }

    public static ComposeFragment newInstance(User currentUser, String title, String url) {

        ComposeFragment composeFragment = new ComposeFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_CURRENT_USER, Parcels.wrap(currentUser));
        args.putString(KEY_TITLE, title);
        args.putString(KEY_URL, url);

        composeFragment.setArguments(args);

        return composeFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        User currentUser = (User) Parcels.unwrap(getArguments().getParcelable(KEY_CURRENT_USER));

        String title = getArguments().getString(KEY_TITLE);
        String url = getArguments().getString(KEY_URL);

        // Setup bindings
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compose, container, false);

        View fragmentView = binding.getRoot();

        ComposeViewModel composeViewModel = new ComposeViewModel((NewTweetsListener) getActivity(), currentUser);
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

        setupSaveDraft();

        if (title.isEmpty() && url.isEmpty()) {
            setDraftIfExists();
        } else {
            prefillTweet(title, url);
        }

        return fragmentView;
    }

    @Override
    public void onResume() {

        int height = getResources().getDimensionPixelSize(R.dimen.dialog_compose_height);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);

        super.onResume();
    }

    private void setupSaveDraft() {

        binding.btSaveDraft.setOnClickListener((view) -> {

            String newTweet = binding.etNewTweet.getText().toString();

            Draft draft = new Draft(newTweet);
            draft.saveNew();
            dismiss();
        });
    }

    private void setDraftIfExists() {

        String draft = Draft.getLast();

        if (draft != null && !draft.isEmpty()) {

            binding.etNewTweet.setText(draft);

            int selectionPosition = draft.length();
            binding.etNewTweet.setSelection(selectionPosition);
        }
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
        binding.btSaveDraft.setTypeface(Fonts.fontExtraBold);
        binding.tvCharacterCount.setTypeface(Fonts.fontRegular);
    }

    private void setupClickEvents(final ComposeViewModel composeViewModel) {

        binding.btSendTweet.setOnClickListener((view) -> {

            String newTweet = binding.etNewTweet.getText().toString();
            composeViewModel.sendNewTweet(newTweet);

            Draft.deleteAll(); //clear draft
            dismiss();
        });

        binding.ivCloseDialog.setOnClickListener((view) -> {

            dismiss();
        });

        final TextWatcher characterCountWatcher = getCharacterCountWatcher(composeViewModel);

        binding.etNewTweet.addTextChangedListener(characterCountWatcher);
    }

    public TextWatcher getCharacterCountWatcher(final ComposeViewModel composeViewModel) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int currentCount = composeViewModel.getCharacterCount(s.length());

                if (currentCount <= 0) {
                    binding.btSendTweet.setEnabled(false);
                    binding.tvCharacterCount.setText("Too many characters.");
                    binding.tvCharacterCount.setTextColor(Color.RED);
                } else {
                    binding.btSendTweet.setEnabled(true);
                    binding.tvCharacterCount.setText(String.valueOf(currentCount));
                    binding.tvCharacterCount.setTextColor(Color.BLACK);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public void prefillTweet(String title, String url) {

        binding.etNewTweet.setText(title + " " + url);
    }
}
