package com.example.skiescue.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skiescue.favourite.viewmodel.FavouriteViewModel
import com.example.skiescue.model.Repository

class HomeViewModelFactory(private val repository: Repository): ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // return HomeViewModel(this.repository) as T

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else {
            throw IllegalArgumentException("View Model class Not found")
        }
    }
}