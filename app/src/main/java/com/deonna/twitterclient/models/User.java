package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class User {

    private static final String SMALL_IMAGE_TEXT = "_normal";

    @SerializedName("name")
    @Expose
    public String name = null;

    @SerializedName("location")
    @Expose
    public String location = null;

    @SerializedName("profile_image_url_https")
    @Expose
    public String profileImageUrl = null;

    @SerializedName("verified")
    @Expose
    public Boolean isVerified = null;

    @SerializedName("screen_name")
    @Expose
    public String screenName = null;

    public String getLargeProfileImageUrl() {

        return  profileImageUrl.replaceAll(SMALL_IMAGE_TEXT, "");
    }
}
