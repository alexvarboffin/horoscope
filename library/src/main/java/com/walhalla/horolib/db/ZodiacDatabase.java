package com.walhalla.horolib.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ZodiacSign.class}, version = 2)
public abstract class ZodiacDatabase extends RoomDatabase {
    public abstract ZodiacSignDao zodiacSignDao();
}

