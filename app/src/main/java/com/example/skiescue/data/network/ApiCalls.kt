package com.example.skiescue.data.network
import com.example.skiescue.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val appId = "bec88e8dd2446515300a492c3862a10e"
private const val excludeMinutes = "minutely"

interface ApiCalls {

        @GET("onecall")
        suspend fun getCurrentWeather(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            //@Query("lang") language: String="ar",
            //@Query("units") units: String,
            @Query("exclude") exclude: String = excludeMinutes,
            @Query("appid") appid: String = appId
        ): Response<WeatherResponse>

}
