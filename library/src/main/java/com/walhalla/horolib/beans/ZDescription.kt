package com.walhalla.horolib.beans

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by combo on 10/22/2017.
 */
@Keep
class ZDescription : Serializable {
    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("sign")
    @Expose
    var sign: String = ""

    @SerializedName("symbol")
    @Expose
    var symbol: String = ""

    @SerializedName("planet")
    @Expose
    var planet: String = ""

    @SerializedName("element")
    @Expose
    var element: String = ""

    @JvmField
    @SerializedName("description")
    @Expose
    var description: String = ""

    @JvmField
    @SerializedName("man")
    @Expose
    var man: String = ""

    @JvmField
    @SerializedName("woman")
    @Expose
    var woman: String = ""

    @JvmField
    @SerializedName("kids")
    @Expose
    var kids: String = ""

    @JvmField
    @SerializedName("love")
    @Expose
    var love: String = ""

    @JvmField
    @SerializedName("career")
    @Expose
    var career: String = ""

    @SerializedName("culinary")
    @Expose
    var culinary: String = ""

    @JvmField
    @SerializedName("sexuality")
    @Expose
    var sexuality: String = ""

    @JvmField
    @SerializedName("health")
    @Expose
    var health: String = ""

    @JvmField
    @SerializedName("compatibility")
    @Expose
    var compatibility: String = ""
}
