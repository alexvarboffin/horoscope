package com.walhalla.horolib.rest

import com.walhalla.horolib.beans.ZDescription
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by combo on 10/14/2017.
 */
interface NetworkService {
    //    @GET("/apps/horo/v2/getHoroscopeBySignTTY")
    //    Call<ForecastResponse> getHoroscope(
    //            @Query("sign_id") int sign_id
    //    );
    @GET("/apps/horo/v2/getHoroscopeBySignTTY")
    fun getHoroscope(
        //        @Query("sign_id") int sign_id
        @QueryMap options: MutableMap<String, String>
    ): Call<ForecastResponse>?

    //    @GET("/api/")
    //    Call<ForecastResponse> getHoroscope(
    //            //        @Query("sign_id") int sign_id
    //            @QueryMap Map<String, String> options
    //    );
    //    admin panel
    //    @GET("/api/forecast/{id}")
    //    Call<ForecastResponse> getHoroscope(
    //            @Path(value = "id") int id
    //    );
    @GET("/{id}")
    fun getHoroscopeDescription(@Path(value = "id") id: Int): Call<ZDescription>?
}
