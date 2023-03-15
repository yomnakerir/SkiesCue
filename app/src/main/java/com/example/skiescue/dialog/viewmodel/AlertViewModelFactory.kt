package com.example.skiescue.dialog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skiescue.model.Repository

class AlertViewModelFactory  (private var repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            return AlertViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not found")
        }
    }
}