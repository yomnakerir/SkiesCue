package com.example.skiescue.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private val locationProvide: MutableLiveData<Int> = MutableLiveData()
    val selectedLocProvider: LiveData<Int> get() = locationProvide
    fun selectedLocProvider(item: Int) {
        locationProvide.value = item
    }
}