package com.deonna.twitterclient.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

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

    public static Gson gson = new GsonBuilder().create();

    public static User fromJson(JSONObject userJson) {

        User user = gson.fromJson(
                userJson.toString(),
                new TypeToken<User>() {
                }.getType());

        return user;
    }
}
