package ai.horo.astrolife.free.gethoroscope.horoscope;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.ftinc.scoop.Scoop;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.walhalla.MBaseApplication;
import com.walhalla.domain.repository.from_internet.AdvertAdmobRepository;
import com.walhalla.domain.repository.from_internet.AdvertConfig;

import java.util.ArrayList;
import java.util.List;



public class Application extends MBaseApplication {

    public static AdvertAdmobRepository repository;


    public static final boolean DEBUG = true;


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


}
