package com.example.skiescue.favourite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skiescue.data.local.Favourite
import com.example.skiescue.model.Repository
import kotlinx.coroutines.launch

class FavouriteViewModel (private val repo: Repository): ViewModel(){

   private val _favouriteList = MutableLiveData<List<Favourite>>()
            val favouriteList:LiveData<List<Favourite>>
            get() = _favouriteList

   fun getFavouriteList(){
       viewModelScope.launch {
           _favouriteList.value = repo.getFavourites()
       }
   }

   fun deleteFavourite(favourite: Favourite){
       viewModelScope.launch {
           repo.deleteFavourite(favourite)
       }
   }

   fun insertFavourite(favourite: Favourite){
       viewModelScope.launch {
           repo.insertFavourite(favourite)
       }
   }
}