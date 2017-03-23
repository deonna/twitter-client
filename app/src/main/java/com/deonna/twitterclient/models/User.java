package com.deonna.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    @Expose
    public String name = null;

    @SerializedName("location")
    @Expose
    public String location = null;

    @SerializedName("profile_image_url_https")
    @Expose
    public String profileImageUrl = null;
}
