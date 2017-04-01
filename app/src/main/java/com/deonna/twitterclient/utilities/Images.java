package com.deonna.twitterclient.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.deonna.twitterclient.viewmodels.TweetDetailViewModel;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Images {

    public static final int ROUNDED_CORNER_RADIUS = 10;
    public static final int ROUNDED_CORNER_MARGIN = 2;

    public static void loadFromUrl(
            Context context,
            ImageView ivImage,
            String url,
            int height,
            int width) {

        int size = TweetDetailViewModel.getProfileImageSize();

        Glide.with(context)
                .load(url)
                .override(size, size)
                .bitmapTransform(
                        new RoundedCornersTransformation(
                                context,
                                ROUNDED_CORNER_RADIUS,
                                ROUNDED_CORNER_MARGIN)
                )
                .into(ivImage);
    }

    public static void loadFromUrlWithFixedSize(
            Context context,
            ImageView ivImage,
            String url) {

        Glide.with(context)
                .load(url)
                .bitmapTransform(
                        new RoundedCornersTransformation(
                                context,
                                ROUNDED_CORNER_RADIUS,
                                ROUNDED_CORNER_MARGIN)
                )
                .into(ivImage);
    }

    public static void loadFromUrlWithFixedSizeRoundedTop(
            Context context,
            ImageView ivImage,
            String url) {

        Glide.with(context)
                .load(url)
                .bitmapTransform(
                        new RoundedCornersTransformation(
                                context,
                                50,
                                ROUNDED_CORNER_MARGIN,
                                RoundedCornersTransformation.CornerType.TOP)
                )
                .into(ivImage);
    }
}
