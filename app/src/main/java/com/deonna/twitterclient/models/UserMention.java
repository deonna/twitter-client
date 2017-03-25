package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class UserMention {

    @SerializedName("screen_name")
    @Expose
    public String screenName;

    @SerializedName("indices")
    @Expose
    public List<Integer> indicies;
}
