package com.example.skiescue.data

import com.example.skiescue.model.WeatherResponse

sealed class ApiResponse <T>{
    class OnSucess<T>(var data: T):ApiResponse<T>()
    class OnLoading<T>():ApiResponse<T>()
    class onError<T>(var message: String):ApiResponse<T>()
}
