package com.deonna.twitterclient.models;

import com.deonna.twitterclient.database.TwitterClientDatabase;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.parceler.Parcel;

@Parcel(analyze = VideoStats.class)
@Table(database = TwitterClientDatabase.class)
public class VideoStats {

    @Column
    @PrimaryKey(autoincrement=true)
    long id;

    @Column
    @SerializedName("content_type")
    @Expose
    String contentType;

    @Column
    @SerializedName("url")
    @Expose
    String url;
}
