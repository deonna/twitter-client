package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deonna on 3/26/17.
 */

public class ExtendedEntities {

    @SerializedName("type")
    @Expose
    public String media;

    @SerializedName("url")
    @Expose
    public List<UserMention> videoUrl;
}
