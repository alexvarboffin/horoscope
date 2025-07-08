package com.walhalla.horolib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import androidx.annotation.NonNull;

import com.walhalla.horolib.R;
import com.walhalla.ui.BuildConfig;

/**
 * Created by combo on 18.09.2017.
 */

public class Utils {

    private static final boolean DEBUG = BuildConfig.DEBUG;


        /*
    * GMT+02:00
      GMT+0300
    * */


    public static String time_zone() {
        Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("Z");
        return "GMT" + dateFormat.format(instance.getTime());
    }

    public static boolean isNetworkAvailable0(@NonNull Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isUnlock(Context context) {
        //return "ru".contains(Locale.getDefault().getLanguage().toLowerCase());
        return true;//context.getString(R.string.default_location).contains("ru");
    }

    public static String getTimezone() {
        return Utils.time_zone(); //"GMT+0300";
//            TimeZone.getTimeZone("GMT").toString().replace(":","");

//            TimeZone.getDefault()
//            .getDisplayName(false, TimeZone.SHORT).replace(":", "");
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}