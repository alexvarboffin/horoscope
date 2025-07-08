package com.walhalla.horolib.helper;

/**
 * Created by combo on 10/22/2017.
 */

public interface ParentFragmentCallback {

    void onFinish(String tag, boolean state);

    void refreshData();

    void shareData(String buffer);
}
