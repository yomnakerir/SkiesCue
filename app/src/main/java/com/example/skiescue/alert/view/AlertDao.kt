package com.example.skiescue.alert.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow



@Dao
interface AlertDao {

    @Query("SELECT * FROM alert")
    fun getAlerts(): Flow<List<AlertModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertModel):Long

    @Query("DELETE FROM alert WHERE id = :id")
    suspend fun deleteAlert(id: Int)

    @Query("SELECT * FROM alert WHERE id = :id")
    suspend fun getAlert(id: Int):AlertModel

}