package com.deonna.twitterclient.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    private static final String SMALL_IMAGE_TEXT = "_normal";

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("location")
    @Expose
    public String location;

    @SerializedName("profile_image_url_https")
    @Expose
    public String profileImageUrl;

    @SerializedName("verified")
    @Expose
    public Boolean isVerified;

    @SerializedName("screen_name")
    @Expose
    public String screenName;

    @SerializedName("profile_background_image_url")
    @Expose
    public String profileBackgroundImageUrl;

    @SerializedName("friends_count")
    @Expose
    public int numFollowing;

    @SerializedName("followers_count")
    @Expose
    public int numFollowers;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("description")
    @Expose
    public String description;

    public String getLargeProfileImageUrl() {

        return  profileImageUrl.replaceAll(SMALL_IMAGE_TEXT, "");
    }

    public static Gson gson = new GsonBuilder().create();

    public static User fromJson(JSONObject userJson) {

        User user = gson.fromJson(
                userJson.toString(),
                new TypeToken<User>() {
                }.getType());

        return user;
    }

    public String getBackgroundImageUrl() {

        return profileBackgroundImageUrl;
    }
}
