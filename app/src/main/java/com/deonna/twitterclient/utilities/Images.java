package com.deonna.twitterclient.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
            int width
    ) {

        Glide.with(context)
                .load(url)
                .override(height, width)
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
            String url
    ) {

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

    public static void loadFromUrlWithFixedSizeRegularCorners(
            Context context,
            ImageView ivImage,
            String url
    ) {

        Glide.with(context)
                .load(url)
                .into(ivImage);
    }

    public static void loadCircularImage(
            Context context,
            ImageView ivImage,
            String url
    ) {

        Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivImage.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static void loadFromUrlWithFixedSizeRoundedTop(
            Context context,
            ImageView ivImage,
            String url
    ) {

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
