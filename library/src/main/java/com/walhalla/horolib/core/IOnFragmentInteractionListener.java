package com.walhalla.horolib.core;


import androidx.fragment.app.Fragment;

import com.walhalla.horolib.beans.ZodiacSignAstro;

public interface IOnFragmentInteractionListener {
    void replaceFragmentRequest(Fragment fragment);

    void setActionBarTitle(int title);

    void setActionBarTitle(String title);

    void showBottomBanner(boolean b);

    void onClickGetLessonRequest(ZodiacSignAstro zodiacSignAstro);

    void makeToast(String error);

    void rootMoreFragment(ZodiacSignAstro o);

    void showGallery();
}

