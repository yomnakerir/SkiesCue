package com.example.skiescue.model

import android.content.Context
import com.example.skiescue.alert.view.AlertModel
import com.example.skiescue.data.local.Favourite
import com.example.skiescue.data.local.RoomDB
import com.example.skiescue.data.network.ApiCalls
import com.example.skiescue.data.network.RetrofitInstance
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Repository (context: Context){
    private lateinit var remote: ApiCalls
    private lateinit var room: RoomDB

    init{
         remote = RetrofitInstance().apiCall()
         room = RoomDB.invoke(context)
    }

    // functions from fav dao

     fun getFavourites(): Flow<List<Favourite>> {
       return room.favouriteDao().getFavourites()
    }

    suspend fun insertFavourite(favourite: Favourite){
     room.favouriteDao().insertFavourite(favourite)
    }


    suspend fun deleteFavourite(favourite: Favourite){
        room.favouriteDao().deleteFavourite(favourite)
    }


    // alerts

    fun getAlerts(): Flow<List<AlertModel>>{
       return room.alertDao().getAlerts()
    }


    suspend fun insertAlert(alert: AlertModel):Long{
     return room.alertDao().insertAlert(alert)
    }


    suspend fun deleteAlert(id: Int){
        room.alertDao().deleteAlert(id)
    }


    suspend fun getAlert(id: Int): AlertModel{
        return room.alertDao().getAlert(id)
    }




    // functions from Api calls
    fun getWeatherDetalis(
        lat: Double,
        lon: Double,
        language: String ,
        units: String ,
        exclude: String ?= null,

    ) = flow {
        val response =  remote.getWeatherDetalis(lat = lat, lon = lon, exclude = exclude, units = units, language = language)
        if(response.isSuccessful){
            emit(response.body() ?: WeatherResponse() )
        }else{
        emit( WeatherResponse() ) }
    }
}