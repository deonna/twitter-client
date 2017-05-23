package com.deonna.twitterclient.utilities;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Times {

    private static final String DATE_PATTERN = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    public static String getRelativeTimestamp(String createdAt) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
        format.setLenient(true);

        String relativeDate = "";

        try {
            long dateMillis = format.parse(createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
