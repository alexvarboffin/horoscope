package com.walhalla.horolib.di.module;

import android.app.Application;
import android.content.Context;
import android.content.NonN;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.ftinc.scoop.Scoop;

import com.walhalla.horolib.R;
import com.walhalla.horolib.rest.NetworkService;
import com.walhalla.horolib.LocalStorage;

import com.walhalla.horolib.repository.HoroscopeRepository;
import com.walhalla.horolib.repository.HoroscopeRepositoryImpl;
import com.walhalla.ui.BuildConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class AppModule {

    private final Application application;

    public AppModule(@NonNull Application application) {
        this.application = application;
    }

    @Provides
    @NonNull
    @Singleton
    ViewPump provideCalligraphyDefaultConfig(Context context) {
        return ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath(context.getString(R.string.defFontName))
                                .setFontAttrId(R.attr.fontPath)
                                .build())).build();
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    public LocalStorage provideLocalStorage(Context context) {
        return new LocalStorage(context);
    }

    @Provides
    @Singleton
    public HoroscopeRepository provideLessonRepository(Context context) {
        return new HoroscopeRepositoryImpl(context);
    }


    @Provides
    @Singleton
    public Scoop.Builder provideScoop(Context context) {
        Scoop.Builder builder = Scoop.waffleCone()
                .addFlavor("Default", R.style.Theme_Scoop_Sea, true)//Theme_Scoop

                .addFlavor("Sand", R.style.Theme_Scoop_Sand)
                .addFlavor("Orange", R.style.Theme_Scoop_Orange)
                .addFlavor("Candy", R.style.Theme_Scoop_Candy)
                .addFlavor("Blossom", R.style.Theme_Scoop_Blossom)
                .addFlavor("Grape", R.style.Theme_Scoop_Grape)
                .addFlavor("Deep", R.style.Theme_Scoop_Deep)
                .addFlavor("Sky", R.style.Theme_Scoop_Sky)
                .addFlavor("Grass", R.style.Theme_Scoop_Grass)
                .addFlavor("Dark", R.style.Theme_Scoop_Dark)
                .addFlavor("Snow", R.style.Theme_Scoop_Snow)
                .addFlavor("Sea", R.style.Theme_Scoop_Sea)
                .addFlavor("Blood", R.style.Theme_Scoop_Blood)

                .addFlavor("Light", R.style.Theme_Scoop_Light);

        //.addDayNightFlavor("DayNight", R.style.Theme_Scoop_DayNight);
        //.addFlavor("Alternate 1", R.style.Theme_Scoop_Alt1)
        //.addFlavor("Alternate 2", R.style.Theme_Scoop_Alt2)//, R.style.Theme_Scoop_Al2_Dialog
        //attachStyle(builder);
        builder.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        return builder;
    }


    @Provides
    @Singleton
    NetworkService provideNetworkService() {
        NetworkService networkService;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // add your other interceptors …

        builder.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);  // <-- this is the important line!
        }

//                .addInterceptor(chain -> {
//
//                 /*   String uid = "0";
//                    long timestamp = (int) (Calendar.getInstance().getTimeInMillis() / 1000);
//                    String signature = MD5Util.crypt(timestamp + "" + uid + MD5_SIGN);
//                    String base64encode = signature + ":" + timestamp + ":" + uid;
//                    base64encode = Base64.encodeToString(base64encode.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
//                */
//                    Request original = chain.request();
//
//
//                    d(String.format("\nRequest:\n%s\nheaders:\n%s",
//                            (original.body() == null) ? null : original.body().toString(),
//                            original.headers()));
//
//                    HttpUrl url = original.url();
////
////                                .newBuilder()
//////                                .addQueryParameter("apikey", "hmiflt4l1ku1ud5lsxz8di3olct1xo2b")
//////                                .addQueryParameter("appid", "0000000000")
////                                //.addQueryParameter("method", "getAliasList")
////                                //.addQueryParameter("kkkkkkk", "00000000000000")
////                                .build();
//
//                    Request request = original.newBuilder()
//                            //.addHeader("Authorization", "zui " + base64encode)
////                                .addHeader("from_client", "sms-reg")
//                            .addHeader("Cache-Control", "no-cache, must-revalidate, max-age=0")
//                            //.url(url)
//                            .build();
//
//
//                    d("initialize: " + url);
//                    d(request.body().toString());
//
//
//                    Response response = chain.proceed(request);
//                    d("Code : " + response.code());
//                    d(response.toString(), response.body().string());
//
//                    if (response.code() == 401) {
//                        // Magic is here ( Handle the error as your way )
//
//                        return response;
//                    }
//                    return response;
//                });

        //"http://mobs.mail.ru"
        //"http://whoesource.tk/horoscope"
        //http://prilapk.pro
        //http://patch-games.ru/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NonN.decode(NonN.d))
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        networkService = retrofit.create(NetworkService.class);
        return networkService;
    }
}
