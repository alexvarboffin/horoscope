package com.walhalla.horolib.repository;

import android.content.Context;
import android.content.res.TypedArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.walhalla.horolib.R;
import com.walhalla.horolib.beans.ZodiacSignAstro;

import java.util.ArrayList;
import java.util.List;


public class HoroscopeRepositoryImpl implements HoroscopeRepository {

    private Context mContext;


    public HoroscopeRepositoryImpl(Context context) {
        this.mContext = context;
    }

    private Gson gson = new GsonBuilder()
            //.setPrettyPrinting()
            .create();

//    public void create() {
//        //FileUtil.copyAsset(mContext.getAssets(),
// "lessons_dataset.json",
// FILE_LESSONS);
//
//    }


    @Override
    public List<ZodiacSignAstro> query(Context context) {
//        try {
//            return getGame().getApp().getZodiacSign();
//            //return gson.fromJson(reader,
// new TypeToken<List<ZodiacSign>>() {}.getType());
//
//        } catch (FileNotFoundException e) {
//            Log.d(TAG,
// "getAllLessons: First app run!");
//            String json = FileUtil.loadJSONFromAsset(mContext.getAssets(),
// "lessons_dataset.json");
//            Game game = gson.fromJson(json,
// Game.class);
//            List<ZodiacSign> list = game.getApp().getZodiacSign();
//
//            Collections.sort(list,
// new Comparator<ZodiacSign>() {
//                public int compare(ZodiacSign c1,
// ZodiacSign c2) {
//                    if (c1.getOrder() < c2.getOrder()) return -1;
//                    if (c1.getOrder() > c2.getOrder()) return 1;
//                    return 0;
//                }
//            });
//
//            //isUnlock
//            int lockCount = 4; //(int) (list.size() * 0.20);
//            for (int i = 0; i < list.size(); i++) {
//                int mmm = (i < lockCount)
//                        ? LessonState.UNLOCK.getValue()
//                        : LessonState.LOCK.getValue();
//                list.get(i).setLock(mmm);
//            }
//
//
//            //save data
//            game.getApp().setZodiacSign(list);
//            json = gson.toJson(game);
//            FileUtil.createAndSaveFile(FILE_LESSONS,
// json);
//
//            return list;
////                return gson.fromJson(reader,
// new TypeToken<List<ZodiacSign>>() {}.getType());
//
//
////            FileUtil.createAndSaveFile(FILE_LESSONS,
// "");
////            try {
////                reader = new FileReader(FILE_LESSONS);
////                Game game = gson.fromJson(reader,
// Game.class);
////                DLog.d(game.toString());
////                return game.getApp().getZodiacSign();
////                //return gson.fromJson(reader,
// new TypeToken<List<ZodiacSign>>() {}.getType());
////
////
////            } catch (FileNotFoundException e1) {
////                e1.printStackTrace();
////            }
//        }
//
//
//        /*if (json == null) {
//            JsonUtil.mCreateAndSaveFile(CFG,
// "[]");
//            json = JsonUtil.mReadJsonData(CFG);
//        }*/


        List<ZodiacSignAstro> list = new ArrayList<>();

        final String[] name = context.getResources().getStringArray(R.array.sign_name);
        final String[] description = context.getResources().getStringArray(R.array.sub_description);

        final TypedArray t1 = context.getResources().obtainTypedArray(R.array.icon);
        final TypedArray t2 = context.getResources().obtainTypedArray(R.array.icon2);
        final TypedArray t3 = context.getResources().obtainTypedArray(R.array.colors);

//        int[] colors = new int[t3.length()];
//        for (int i = 0; i < t3.length(); i++) {
//            //colors[i] = t3.getColor(i, 0);
//        }


        for (int i = 0; i < name.length; i++) {
            final ZodiacSignAstro obj = new ZodiacSignAstro(
                    i + 1, name[i], description[i],
                    t1.getResourceId(i, -1),
                    t2.getResourceId(i, -1),
                    t3.getResourceId(i, -1)
            );
            list.add(obj);
        }
        t1.recycle();
        t2.recycle();
        t3.recycle();
        return list;
    }
}
