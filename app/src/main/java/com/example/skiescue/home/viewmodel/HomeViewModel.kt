package com.example.skiescue.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skiescue.data.local.Favourite
import com.example.skiescue.model.Repository
import com.example.skiescue.model.WeatherResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: Repository) : ViewModel() {

    // for dialog
    private val locationProvide: MutableLiveData<Int> = MutableLiveData()
    val selectedLocProvider: LiveData<Int> get() = locationProvide
    fun selectedLocProvider(item: Int) {
        locationProvide.value = item
    }

    private val _weatherDetails = MutableLiveData<WeatherResponse>()
    val weatherDetails:LiveData<WeatherResponse>
    get() = _weatherDetails

    fun getWeatherDetails(
        lat:Double,
        lon: Double,
        exclude: String ?= null ){
         // here the data come , i wil send it by live data
        viewModelScope.launch {
           // _weatherDetails.value = repo.getWeatherDetalis(lat, lon, exclude)

            // for test
            val data = repo.getWeatherDetalis(lat, lon, exclude)
            repo.insertFavourite(Favourite(weather = data))
            _weatherDetails.value = data


        }

    }

}