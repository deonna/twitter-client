package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Entities {

    @SerializedName("media")
    @Expose
    public List<Medium> media = null;
}

