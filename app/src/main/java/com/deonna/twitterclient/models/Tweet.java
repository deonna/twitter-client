package com.deonna.twitterclient.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    @SerializedName("id")
    @Expose
    public Long id;

    @SerializedName("text")
    @Expose
    public String text;

    @SerializedName("user")
    @Expose
    public User user;

    @SerializedName("retweet_count")
    @Expose
    public String retweetCount;

    @SerializedName("favorite_count")
    @Expose
    public String favoriteCount;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("entities")
    @Expose
    public Entities entities;

    @SerializedName("favorited")
    @Expose
    public boolean favorited;

    @SerializedName("retweeted")
    @Expose
    public boolean retweeted;

    public static Gson gson = new GsonBuilder().create();

    public static List<Tweet> fromJson(JSONArray tweetsJson) {

        List<Tweet> tweets = gson.fromJson(
                tweetsJson.toString(),
                new TypeToken<ArrayList<Tweet>>() {
                }.getType());

        return tweets;
    }
}
