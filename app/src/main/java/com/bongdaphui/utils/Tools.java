package com.bongdaphui.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {

    public static String getFormattedDateSimple(Long dateTime) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
        return newFormat.format(new Date(dateTime));
    }

}
