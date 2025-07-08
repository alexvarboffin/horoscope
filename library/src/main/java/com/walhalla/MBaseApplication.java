package com.walhalla;

import androidx.multidex.MultiDexApplication;

import com.ftinc.scoop.Scoop;

import com.walhalla.horolib.di.component.ApplicationComponent;
import com.walhalla.horolib.di.component.DaggerApplicationComponent;
import com.walhalla.horolib.di.module.AppModule;
import com.walhalla.ui.DLog;


import javax.inject.Inject;

import io.github.inflationx.viewpump.ViewPump;

public abstract class MBaseApplication extends MultiDexApplication {


    public static ApplicationComponent applicationComponent;


    @Inject
    ViewPump pump;

    @Inject
    Scoop.Builder scoop;

    @Override
    public void onCreate() {
        super.onCreate();


        //Fabric.with(this, new Crashlytics());

        applicationComponent = DaggerApplicationComponent.builder().appModule(new AppModule(this)).build();
        applicationComponent.inject(this);


        scoop.initialize();
        ViewPump.init(pump);
    }

    public static ApplicationComponent getAppComponent() {
        return applicationComponent;
    }
}
