package com.example.skiescue.alert.view

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert")
data class AlertModel(

    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    var startTime: Long ?= null,
    var endTime: Long ?= null,
    var startDate: Long ?= null,
    var endDate: Long ?= null,

    var latitude: Double ?= null,
    var longitude: Double ?= null,
)