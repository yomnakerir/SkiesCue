package com.example.skiescue.data.network
import com.example.skiescue.Constants
import com.example.skiescue.model.Units
import com.example.skiescue.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiCalls {

        @GET("onecall")
        suspend fun getWeatherDetalis(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("lang") language: String="ar",
            @Query("units") units: String,
            @Query("exclude") exclude: String ?= null,
            @Query("appid") appid: String = Constants.appId
        ): Response<WeatherResponse>

}
