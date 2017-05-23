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
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.util.List;

@Parcel(analyze = VideoInfo.class)
@Table(database = TwitterClientDatabase.class)
public class VideoInfo {

    @Column
    @PrimaryKey(autoincrement=true)
    long id;

    @SerializedName("variants")
    @Expose
    List<VideoStats> videoStats;

    public static Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy[]{ new DbFlowExclusionStrategy() })
            .create();

    public static List<VideoInfo> fromJsonMultiple(JSONArray videoInfoListJson) {

        List<VideoInfo> videoInfoList = gson.fromJson(
                videoInfoListJson.toString(),
                new TypeToken<List<VideoInfo>>() {
                }.getType());

        return videoInfoList;
    }
}
