package com.example.skiescue.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skiescue.model.Repository

class FavouriteViewModelFactory(private val repository: Repository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       // return FavouriteViewModel(this.repository) as T

        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            return FavouriteViewModel(repository) as T
        } else {
            throw IllegalArgumentException("View Model class Not found")
        }
    }
}