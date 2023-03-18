package com.example.skiescue.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skiescue.data.ApiResponse
import com.example.skiescue.data.local.Favourite
import com.example.skiescue.model.Repository
import com.example.skiescue.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: Repository) : ViewModel() {

    // for dialog
    private val locationProvide: MutableLiveData<Int> = MutableLiveData()
    val selectedLocProvider: LiveData<Int> get() = locationProvide
    fun selectedLocProvider(item: Int) {
        locationProvide.value = item
    }

    private val _weatherDetails = MutableStateFlow<ApiResponse<WeatherResponse>>(ApiResponse.OnLoading())
    val weatherDetails:StateFlow<ApiResponse<WeatherResponse>>
    get() = _weatherDetails



    fun getWeatherDetails(
        lat:Double,
        lon: Double,
        exclude: String ,
        units: String,
        language: String,
        ){
         // here the data come , i wil send it by live data
        viewModelScope.launch {
           // _weatherDetails.value = repo.getWeatherDetalis(lat, lon, exclude, units)

            // for test insertion
            repo.getWeatherDetalis(lat, lon, exclude, units, language)
                .catch {
                    _weatherDetails.value = ApiResponse.onError(it.message.toString())
                }
                .collect(){
                  //  repo.insertFavourite(Favourite(latitude = it.lat, longitude = it.lon, city = it.timezone!!))
                    _weatherDetails.value = ApiResponse.OnSucess(it)

                }


        }

    }

}