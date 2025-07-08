package com.walhalla.horolib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by combo on 10/19/2017.
 */

public class DateExtractor {
    public static String fullData(String dateString) {
        return extract(dateString, "dd MMMM yyyy, EEEE");
    }

    private static String extract(String dateString, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(dateString);

//            String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(date);
//            String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);


            String formatted = new SimpleDateFormat(format, Locale.getDefault()).format(date);

            int month_n = 0;
//            month=Integer.parseInt(formated);

            return formatted;
        } catch (ParseException e) {
            return "";
        }
    }

    public static String extractWeek(String dateString) {
        return extract(dateString, "dd MMMM yyyy, EEEE");
    }
}
