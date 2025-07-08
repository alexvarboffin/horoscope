package com.walhalla.horolib.beans

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walhalla.horolib.rest.ForecastResponse
import java.io.Serializable


@Keep
class SignWrapper(
    @JvmField @field:Expose @field:SerializedName("sign") var sign: ZodiacSignAstro,
    @JvmField @field:Expose @field:SerializedName("forecast") var forecast: ForecastResponse?,
    @JvmField @field:Expose @field:SerializedName("description") val description: ZDescription?
) : Serializable
