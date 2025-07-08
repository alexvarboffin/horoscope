package com.walhalla.horolib.rest

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walhalla.horolib.beans.Prognosis
import java.io.Serializable

/**
 * Created by combo on 10/13/2017.
 */

@Keep
class ForecastResponse : Serializable {
    @JvmField
    @SerializedName("week")
    @Expose
    var week: Prognosis? = Prognosis()

    @JvmField
    @SerializedName("yesterday")
    @Expose
    var yesterday: Prognosis? = Prognosis()

    @JvmField
    @SerializedName("month")
    @Expose
    var month: Prognosis? = Prognosis()


    //array?
    @JvmField
    @SerializedName("year")
    @Expose
    var year: MutableList<Prognosis>? = ArrayList()

    @JvmField
    @SerializedName("tomorrow")
    @Expose
    var tomorrow: Prognosis? = Prognosis()

    @JvmField
    @SerializedName("today")
    @Expose
    var today: Prognosis? = Prognosis()
}
