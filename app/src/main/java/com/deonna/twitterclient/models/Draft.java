package com.deonna.twitterclient.models;

import com.deonna.twitterclient.database.TwitterClientDatabase;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.List;

@Table(database = TwitterClientDatabase.class)
@Parcel(analyze={Draft.class})
public class Draft extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("id")
    long id;

    @Column
    @SerializedName("text")
    public String text;

    public Draft() {}

    public Draft(String text) {

        this.text = text;
    }


    public void saveNew() {

        if (text != null) {
            this.save();
        }
    }

    public static String getLast() {

        List<Draft> draft = SQLite
                .select()
                .from(Draft.class)
                .queryList();

        if (!draft.isEmpty()) {
            return draft.get(draft.size() - 1).text;
        }

        return null;
    }

    public static void deleteAll() {

        Delete.tables(Draft.class);
    }
}
