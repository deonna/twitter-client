package com.deonna.twitterclient.utilities;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Fonts {

    public static Typeface fontBold;
    public static Typeface fontExtraBold;
    public static Typeface fontLight;
    public static Typeface fontRegular;
    public static Typeface fontThin;

    private static String fontBoldName = "Lato-Bold.ttf";
    private static String fontExtraBoldName = "Lato-Black.ttf";
    private static String fontLightName = "Lato-Light.ttf";
    private static String fontRegularName = "Lato-Regular.ttf";
    private static String fontThinName = "Lato-Thin.ttf";

    public static void setupFonts(AssetManager assetManager) {

        fontBold = Typeface.createFromAsset(assetManager, fontBoldName);
        fontExtraBold = Typeface.createFromAsset(assetManager, fontExtraBoldName);
        fontLight = Typeface.createFromAsset(assetManager, fontLightName);
        fontRegular = Typeface.createFromAsset(assetManager, fontRegularName);
        fontThin = Typeface.createFromAsset(assetManager, fontThinName);
    }
}
