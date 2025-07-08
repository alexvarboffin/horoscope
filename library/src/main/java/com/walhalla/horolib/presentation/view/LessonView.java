package com.walhalla.horolib.presentation.view;

import com.walhalla.horolib.beans.ZodiacSignAstro;

import java.util.List;

public interface LessonView {

    void showBottomAds(boolean b);

    void loadData(List<ZodiacSignAstro> list);

    void getLessonRequest(ZodiacSignAstro current);

    void showGallery();
}
