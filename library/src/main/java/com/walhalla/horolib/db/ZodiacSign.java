package com.walhalla.horolib.db;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "zodiac_signs")
public class ZodiacSign {
    public void setSignId(int signId) {
        this.signId = signId;
    }

    @PrimaryKey
    private int signId;
    public String text;

    //public String week;
    //public String year;
    //public String month;

    public int getSignId() {
        return signId;
    }
}
