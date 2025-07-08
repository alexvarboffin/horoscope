package com.walhalla.horolib.beans

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by combo on 10/13/2017.
 */


@Keep
class Prognosis : Serializable {
    @JvmField
    @SerializedName("date")
    @Expose
    var date: String = ""

    @JvmField
    @SerializedName("text")
    @Expose
    var text: String = ""

    //    public String getUrl() {
    //    @SerializedName("url")
    //    @Expose
    //    private String url;
    @SerializedName("sign_id")
    @Expose
    var signId: Int = 1


    //        return url;
    //    }
    //
    //    public void setUrl(String url) {
    //        this.url = url;
    //    }
}
