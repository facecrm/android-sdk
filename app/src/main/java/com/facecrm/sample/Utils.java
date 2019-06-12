package com.facecrm.sample;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static String appId = "24142f7e-4ddf-4154-8c43-0aa3a8736463";
    public static String secretCode = "a0ae0431b760594a1bd157f98b787901b240ef1acd7ef133";
    public static String PREF = "pref";
    public static String ID = "id";
    public static String TOKEN = "token";

    public static String covertTime(String timestampUTC) {
        String FORMAT_UTC = "yyyy-MM-dd'T'hh:mm:ss.SSSSSS'Z'";
        String FORMAT_LOCAL = "yyyy-MM-dd hh:mm:ss";
        String formattedDate = timestampUTC;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_UTC);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = formatter.parse(timestampUTC);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter2 = new SimpleDateFormat(FORMAT_LOCAL);
            formatter2.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));

            formattedDate = formatter2.format(date);
//            Log.e("date time", formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    private static String getCurrentTimeZone() {
        TimeZone tz = Calendar.getInstance().getTimeZone();
        return tz.getID();
    }
}
