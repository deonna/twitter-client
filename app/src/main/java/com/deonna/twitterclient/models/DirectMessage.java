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

@Table(database = TwitterClientDatabase.class)
@Parcel(analyze={DirectMessage.class})
public class DirectMessage {

    @Column
    @PrimaryKey
    @SerializedName("id")
    @Expose
    public Long id;

    @Column
    @SerializedName("recipient_screen_name")
    @Expose
    public String recipientScreenName;

    @Column
    @SerializedName("sender_screen_name")
    @Expose
    public String senderScreenName;

    @Column
    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @Column
    @SerializedName("text")
    @Expose
    public String text;

    @Column
    @ForeignKey
    @SerializedName("recipient")
    @Expose
    public User recipient;

    @Column
    @ForeignKey
    @SerializedName("sender")
    @Expose
    public User sender;

    public static Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy[]{ new DbFlowExclusionStrategy() })
            .create();

    public static List<DirectMessage> fromJsonMultiple(JSONArray directMessagesJson) {

        List<DirectMessage> directMessages = gson.fromJson(
                directMessagesJson.toString(),
                new TypeToken<ArrayList<DirectMessage>>() {
                }.getType());

        return directMessages;
    }
}
