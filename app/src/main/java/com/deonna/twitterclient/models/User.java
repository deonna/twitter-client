package com.deonna.twitterclient.models;

import com.deonna.twitterclient.database.DbFlowExclusionStrategy;
import com.deonna.twitterclient.database.TwitterClientDatabase;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Table(database = TwitterClientDatabase.class)
@Parcel(analyze={User.class})
public class User extends BaseModel {

    private static final String SMALL_IMAGE_TEXT = "_normal";

    @Column
    @SerializedName("name")
    @Expose
    public String name;

    @Column
    @SerializedName("location")
    @Expose
    public String location;

    @Column
    @SerializedName("profile_image_url_https")
    @Expose
    public String profileImageUrl;

    @Column
    @SerializedName("verified")
    @Expose
    public Boolean isVerified;

    @Column
    @PrimaryKey
    @SerializedName("screen_name")
    @Expose
    public String screenName;

    @Column
    @SerializedName("profile_banner_url")
    @Expose
    public String profileBannerUrl;

    @Column
    @SerializedName("friends_count")
    @Expose
    public int numFollowing;

    @Column
    @SerializedName("followers_count")
    @Expose
    public int numFollowers;

    @Column
    @SerializedName("url")
    @Expose
    public String url;

    @Column
    @SerializedName("description")
    @Expose
    public String description;

    public String getLargeProfileImageUrl() {

        return  profileImageUrl.replaceAll(SMALL_IMAGE_TEXT, "");
    }

    public static Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy[]{ new DbFlowExclusionStrategy() })
            .create();

    public static User fromJsonSingle(JSONObject userJson) {

        User user = gson.fromJson(
                userJson.toString(),
                new TypeToken<User>() {
                }.getType());

        return user;
    }

    public String getBannerImageUrl() {

        return profileBannerUrl;
    }

    public static List<User> fromJsonMultiple(JSONArray usersJson) {

        List<User> users = gson.fromJson(
                usersJson.toString(),
                new TypeToken<ArrayList<User>>() {
                }.getType());

        return users;
    }
}
