package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Hashtag {

    @SerializedName("text")
    @Expose
    public String text;

    @SerializedName("indices")
    @Expose
    public List<Integer> indices;
}
