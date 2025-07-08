package com.walhalla.horolib;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.walhalla.ui.DLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CoreUtil {

    //Share image tool
    public static Uri getLocalBitmapUri(Context context, Bitmap bitmap) {
        String APPLICATION_ID = context.getPackageName();
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Quotes_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(context, APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            DLog.handleException(e);
        } catch (IOException e) {
            DLog.handleException(e);
        }
        return bmpUri;
    }

    public static List<String> readFile(Context context, String fileName) {
        List<String> data = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                mLine=mLine.trim();
                if(!mLine.isEmpty()){
                    data.add(mLine);
                }
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return data;
    }
}