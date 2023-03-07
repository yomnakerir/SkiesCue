package com.example.skiescue.data.local

import androidx.room.TypeConverter
import com.example.skiescue.model.WeatherResponse
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun fromStringToWeather(weather:String):WeatherResponse?{
        // pasre string to object

        return weather?.let{
            Gson().fromJson(it, WeatherResponse::class.java)
        }
    }

    @TypeConverter
    fun fromWeatherToString(weather:WeatherResponse):String?{
        // pasre object to string

        return weather?.let{
            Gson().toJson(it)
        }
    }
    }
