package com.walhalla.horolib.presentation.view;

/**
 * Created by combo on 10/22/2017.
 */

public interface LoaderView {
    void showLoading();

    void hideLoading();

    void showError(String string);

    void showError(int error);
}
