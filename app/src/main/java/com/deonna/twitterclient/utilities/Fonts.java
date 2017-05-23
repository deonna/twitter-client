package com.deonna.twitterclient.utilities;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Fonts {

    public static Typeface fontBold;
    public static Typeface fontExtraBold;
    public static Typeface fontLight;
    public static Typeface fontRegular;
    public static Typeface fontThin;

    public static void setupFonts(AssetManager assetManager) {

        fontBold = Typeface.createFromAsset(assetManager, "Lato-Bold.ttf");
        fontExtraBold = Typeface.createFromAsset(assetManager, "Lato-Black.ttf");
        fontLight = Typeface.createFromAsset(assetManager, "Lato-Light.ttf");
        fontRegular = Typeface.createFromAsset(assetManager, "Lato-Regular.ttf");
        fontThin = Typeface.createFromAsset(assetManager, "Lato-Thin.ttf");
    }
}
