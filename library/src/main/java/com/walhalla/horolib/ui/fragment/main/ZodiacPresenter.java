package com.walhalla.horolib.ui.fragment.main;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor;
import com.walhalla.ui.DLog;

import com.walhalla.horolib.db.ZodiacDatabase;
import com.walhalla.horolib.db.ZodiacSign;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.rest.ForecastResponse;

public class ZodiacPresenter {


    private final Handler main;

    interface View {
        void showZodiacForecast(ZodiacSign zodiacSign);

        void showNoForecastAvailable();
    }

    private final ZodiacPresenter.View view;
    private final ZodiacDatabase zodiacDatabase;
    private final ThreadExecutor threadExecutor;

    public ZodiacPresenter(View view, ZodiacDatabase zodiacDatabase, ThreadExecutor threadExecutor, Handler handler) {
        this.view = view;
        this.zodiacDatabase = zodiacDatabase;
        this.threadExecutor = threadExecutor;
        this.main = handler;
    }


    public void getZodiacForecast(final int signId) {
        threadExecutor.execute(() -> {
            try {
                final ZodiacSign zodiacSign = zodiacDatabase.zodiacSignDao().getZodiacSign(signId);
                main.post(() -> {
                    if (zodiacSign != null) {
                        DLog.d("@@@@ db@@@");
                        view.showZodiacForecast(zodiacSign);
                    } else {
                        DLog.d("@@@@ db-empty");
                        view.showNoForecastAvailable();
                    }
                });
            } catch (Exception e) {
                DLog.handleException(e);
            }
        });
    }

    public void insertZodiacSign(ZodiacSignAstro sign, ForecastResponse forecast) {
        threadExecutor.execute(() -> {
            Gson gson = new GsonBuilder().create();
            ZodiacSign mm = new ZodiacSign();
            mm.setSignId(sign.id);
            mm.text = gson.toJson(forecast);
            long mm0 = zodiacDatabase.zodiacSignDao().insertZodiacSign(mm);
            DLog.d("@db-saved@ => " + sign.id + " ==> " + mm0);
        });
    }
}
