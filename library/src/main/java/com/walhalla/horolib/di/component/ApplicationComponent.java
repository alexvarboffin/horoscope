package com.walhalla.horolib.di.component;

import android.app.Activity;

import androidx.multidex.MultiDexApplication;

import com.walhalla.MBaseApplication;
import com.walhalla.horolib.di.module.AppModule;

import com.walhalla.horolib.ui.fragment.HomeFragment;
import com.walhalla.horolib.ui.fragment.RootMoreFragment;
import com.walhalla.horolib.ui.fragment.main.RootForecastsFragment;
import com.walhalla.horolib.ui.fragment.tab.TabForecastFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by combo on 15.09.2017.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface ApplicationComponent {

    void inject(MBaseApplication myApp);

    void inject(Activity mainActivity);

    void inject(HomeFragment homeFragment);

    void inject(RootMoreFragment moreFragment);

    void inject(TabForecastFragment tabForecastFragment);

    void inject(RootForecastsFragment rootForecastsFragment);
}
