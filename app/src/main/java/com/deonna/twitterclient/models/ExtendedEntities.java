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

import java.util.ArrayList;
import java.util.List;

@Parcel(analyze = ExtendedEntities.class)
@Table(database = TwitterClientDatabase.class)
public class ExtendedEntities {

    @Column
    @SerializedName("id")
    @PrimaryKey
    @Expose
    public long id;

    @Column
    @SerializedName("type")
    @Expose
    public String type;

    @Column
    @SerializedName("video_info")
    @Expose
    @ForeignKey
    public VideoInfo videoInfo;

    public static Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy[]{ new DbFlowExclusionStrategy() })
            .create();

    public static List<ExtendedEntities> fromJsonMultiple(JSONArray extendedEntitiesJson) {

        List<ExtendedEntities> extendedEntities = gson.fromJson(
                extendedEntitiesJson.toString(),
                new TypeToken<ArrayList<ExtendedEntities>>() {
                }.getType());

        return extendedEntities;
    }
}
