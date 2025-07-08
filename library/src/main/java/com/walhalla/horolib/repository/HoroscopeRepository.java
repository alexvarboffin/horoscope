package com.walhalla.horolib.repository;

import android.content.Context;

import com.walhalla.horolib.beans.ZodiacSignAstro;

import java.util.List;

/**
 * Created by combo on 15.09.2017.
 */

public interface HoroscopeRepository {
    List<ZodiacSignAstro> query(/*LessonSpecification specification*/Context context);
}
