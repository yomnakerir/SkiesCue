package com.example.skiescue.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourite_table")
    fun getFavourites(): Flow<List<Favourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: Favourite)

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)
}