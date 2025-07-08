package com.walhalla.horolib;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;


public class LocalStorage {

//    private static LocalStorage instance = null;
    private SharedPreferences preferences;

    public LocalStorage(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    //    public synchronized static LocalStorage getInstance() {
//        if (instance == null) {
//            instance = new LocalStorage();
//        }
//
//        return instance;
//    }
    private SharedPreferences sharedPreferences() {
        return preferences;
    }


    public boolean licenseAgree() {
        return preferences.getBoolean("LicenseAgree", false);
    }


    public void licenseAgree(boolean b) {
        preferences.edit().putBoolean("LicenseAgree", b).apply();
    }

}
