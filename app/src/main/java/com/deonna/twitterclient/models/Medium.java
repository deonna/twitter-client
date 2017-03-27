package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Medium {

    @SerializedName("id")
    @Expose
    public Long mediaId;

    @SerializedName("media_url") //can get video - only "photo" for now restirction on "type"
    @Expose
    public String mediaUrl;
}
