package com.deonna.twitterclient.models;

import com.deonna.twitterclient.database.DbFlowExclusionStrategy;
import com.deonna.twitterclient.database.TwitterClientDatabase;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(analyze = Tweet.class)
@Table(database = TwitterClientDatabase.class)
public class Tweet extends BaseModel {

    @SerializedName("id")
    @Expose
    @Column
    @PrimaryKey
    public Long id;

    @Column
    @SerializedName("text")
    @Expose
    public String text;

    @Column
    @ForeignKey
    @SerializedName("user")
    @Expose
    public User user;

    @Column
    @SerializedName("retweet_count")
    @Expose
    public String retweetCount;

    @Column
    @SerializedName("favorite_count")
    @Expose
    public String favoriteCount;

    @Column
    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("entities")
    @Expose
    public Entities entities;

    @Column
    @SerializedName("favorited")
    @Expose
    public boolean favorited;

    @Column
    @SerializedName("retweeted")
    @Expose
    public boolean retweeted;

    public static Gson gson = new GsonBuilder()
        .setExclusionStrategies(new ExclusionStrategy[]{new DbFlowExclusionStrategy()})
        .create();

    public static List<Tweet> fromJson(JSONArray tweetsJson) {

        List<Tweet> tweets = gson.fromJson(
                tweetsJson.toString(),
                new TypeToken<ArrayList<Tweet>>() {
                }.getType());

        return tweets;
    }

    public static List<Tweet> getAll() {
        return SQLite
                .select()
                .from(Tweet.class)
                .orderBy(Tweet_Table.createdAt, false)
                .limit(100)
                .queryList();
    }

    public static void saveAll(List<Tweet> tweets) {

        for (Tweet tweet : tweets) {
            save(tweet);
        }
    }

    private static void save(Tweet tweet) {

        if (tweet != null) {
            if (tweet.user != null) {
                tweet.user.save();
            }
            tweet.save();
        }
    }

    public static void deleteAll() {

        Delete.tables(Tweet.class, User.class);
    }

    public String getMediaUrl() {

        String candidateUrl = "";

        if (entities == null || entities.media == null || entities.media.isEmpty()) {
            return candidateUrl;
        }

        candidateUrl = entities.media.get(0).mediaUrl;

        return candidateUrl;
    }
}
