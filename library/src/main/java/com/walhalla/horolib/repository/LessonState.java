package com.walhalla.horolib.repository;

/**
 * Created by combo on 18.09.2017.
 */

public enum LessonState {
    UNLOCK(0), LOCK(1);

    int value;


    LessonState(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}