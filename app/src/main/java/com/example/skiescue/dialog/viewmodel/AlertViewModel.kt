package com.example.skiescue.dialog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skiescue.alert.view.AlertModel
import com.example.skiescue.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlertViewModel (private val repository: Repository):ViewModel(){

    private val _stateGetAlert = MutableStateFlow<List<AlertModel>>(emptyList())
    val stateGetAlert: StateFlow<List<AlertModel>>
    get() = _stateGetAlert


    private val _stateInsetAlert = MutableStateFlow<Long>(0)
    val stateInsetAlert: StateFlow<Long>
    get() = _stateInsetAlert


    private val _stateSingleAlert = MutableStateFlow<AlertModel>(AlertModel())
    val stateSingleAlert: StateFlow<AlertModel>
        get() = _stateSingleAlert




    fun getAlerts(){
        viewModelScope.launch {
            repository.getAlerts().collect{
              _stateGetAlert.value = it
            }
        }
    }


     fun insertAlert(alert: AlertModel){
         viewModelScope.launch {
             // after insert model
             val id = repository.insertAlert(alert)

             // pass id
             _stateInsetAlert.value = id
         }
     }


     fun deleteAlert(alert: AlertModel){
         viewModelScope.launch{
             repository.deleteAlert(alert.id?:-1)
         }
     }


     fun getAlert(id: Long){

         viewModelScope.launch {
             val alertModel = repository.getAlert(id.toInt())
             _stateSingleAlert.value = alertModel
         }
     }

}