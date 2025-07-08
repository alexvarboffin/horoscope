package com.walhalla.horolib


import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit

class LocalStorage(context: android.content.Context) {
    //    private static LocalStorage instance = null;
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


    //    public synchronized static LocalStorage getInstance() {
    //        if (instance == null) {
    //            instance = new LocalStorage();
    //        }
    //
    //        return instance;
    //    }
    private fun sharedPreferences(): SharedPreferences {
        return preferences
    }


    fun licenseAgree(): kotlin.Boolean {
        return preferences.getBoolean("LicenseAgree", false)
    }


    fun licenseAgree(b: kotlin.Boolean) {
        preferences.edit { putBoolean("LicenseAgree", b) }
    }
}
