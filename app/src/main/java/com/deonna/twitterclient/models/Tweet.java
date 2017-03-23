package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tweet {

    @SerializedName("text")
    @Expose
    public String text = null;

    @SerializedName("user")
    @Expose
    public User user = null;

    @SerializedName("retweet_count")
    @Expose
    public String retweetCount = null;

    @SerializedName("favorite_count")
    @Expose
    public String favoriteCount = null;

    @SerializedName("created_at")
    @Expose
    public String createdAt = null;
}
