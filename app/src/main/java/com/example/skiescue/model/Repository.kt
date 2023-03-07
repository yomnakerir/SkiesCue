package com.example.skiescue.model

import android.content.Context
import com.example.skiescue.Constants
import com.example.skiescue.data.local.Favourite
import com.example.skiescue.data.local.RoomDB
import com.example.skiescue.data.network.ApiCalls
import com.example.skiescue.data.network.RetrofitInstance

import retrofit2.Response


class Repository (context: Context){
    private lateinit var remote: ApiCalls
    private lateinit var room: RoomDB

    init{
         remote = RetrofitInstance().apiCall()
         room = RoomDB.invoke(context)
    }

    // functions from fav dao

    suspend fun getFavourites():List<Favourite>{
       return room.favouriteDao().getFavourites()
    }

    suspend fun insertFavourite(favourite: Favourite){
     room.favouriteDao().insertFavourite(favourite)
    }


    suspend fun deleteFavourite(favourite: Favourite){
        room.favouriteDao().deleteFavourite(favourite)

    }

    // functions from Api calls


    suspend fun getWeatherDetalis(
         lat: Double,
         lon: Double,
        //@Query("lang") language: String="ar",
        //@Query("units") units: String,
        exclude: String ?= null,
    ): WeatherResponse {
        val response =  remote.getWeatherDetalis(lat = lat, lon = lon, exclude = exclude)

        if(response.isSuccessful){
            return response.body() ?: WeatherResponse()
        }
        return WeatherResponse()
    }
}