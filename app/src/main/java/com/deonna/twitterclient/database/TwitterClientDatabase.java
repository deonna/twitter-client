package com.deonna.twitterclient.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TwitterClientDatabase.NAME, version = TwitterClientDatabase.VERSION)
public class TwitterClientDatabase {

    public static final String NAME = "TwitterClientDatabase";

    public static final int VERSION = 1;
}
