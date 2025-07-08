package com.walhalla.horolib.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ZodiacSignDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertZodiacSign(ZodiacSign zodiacSign);

    @Query("SELECT * FROM zodiac_signs WHERE signId = :signId")
    ZodiacSign getZodiacSign(int signId);
}
