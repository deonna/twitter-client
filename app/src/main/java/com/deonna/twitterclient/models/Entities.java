package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Entities {

    @SerializedName("media")
    @Expose
    public List<Medium> media;

    @SerializedName("user_mentions")
    @Expose
    public List<UserMention> userMentions;

    @SerializedName("hashtags")
    @Expose
    public List<Hashtag> hashtags;
}

