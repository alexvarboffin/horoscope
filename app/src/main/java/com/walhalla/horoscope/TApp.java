package com.walhalla.horoscope;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.walhalla.MBaseApplication;
import com.walhalla.domain.repository.from_internet.AdvertAdmobRepository;
import com.walhalla.domain.repository.from_internet.AdvertConfig;
import com.walhalla.wads.AppOpenAdManager;
import com.walhalla.wads.DLog;
import com.walhalla.wads.OnShowAdCompleteListener;

import java.util.ArrayList;
import java.util.List;


public class TApp extends MBaseApplication
        implements Application.ActivityLifecycleCallbacks {

    public static AdvertAdmobRepository repository;
    private static final boolean ACTIVITY_MOVES_TO_FOREGROUND_HANDLE = true;
    
    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);

        List<String> testDevices = new ArrayList<>();
        if (BuildConfig.DEBUG) {
            testDevices.add(AdRequest.DEVICE_ID_EMULATOR);
            testDevices.add("90BB024CB7214225E67797AD71A77EA4");
            testDevices.add("1B4828DBBDF2F51344A4C521B15D76E0");
        }
        RequestConfiguration conf = new RequestConfiguration.Builder()
                //.SetTagForChildDirectedTreatment(RequestConfiguration.TagForChildDirectedTreatmentTrue)
                //.SetMaxAdContentRating(MAX_AD_CONTENT_RATING_T)MAX_AD_CONTENT_RATING_G
                .setTestDeviceIds(testDevices)
                .build();
        MobileAds.setRequestConfiguration(conf);
        MobileAds.initialize(this, initializationStatus -> {
            //getString(R.string.app_id)
        });
        AdvertConfig config = AdvertConfig.newBuilder()
                .setAppId(getString(R.string.app_id))
                .setBannerId(getString(R.string.b1))
                .build();

        repository = AdvertAdmobRepository.getInstance(config);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onStart(@NonNull LifecycleOwner owner) {
                // Show the ad (if available) when the app moves to foreground.
                DLog.d("@@@@@@x" + currentActivity);
                if (ACTIVITY_MOVES_TO_FOREGROUND_HANDLE && currentActivity != null) {
                    appOpenAdManager.showAdIfAvailable(currentActivity);
                }
            }});



        appOpenAdManager = new AppOpenAdManager(getString(R.string.r1));
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.

//        if (!appOpenAdManager.isShowingAd) {
//            currentActivity = activity;
//        }

        if (activity instanceof com.google.android.gms.ads.AdActivity) {

        } else {
            currentActivity = activity;
            if (BuildConfig.DEBUG) {
                //java.lang.Exception: Toast callstack! strTip=###activity.MainActivity
                //Toast.makeText(myApplication, "###" + activity.getLocalClassName(), Toast.LENGTH_SHORT).show();
                DLog.d("###" + activity.getLocalClassName());
            }
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }
}