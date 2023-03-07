package com.example.skiescue.data.local

import androidx.room.*

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourite_table")
    suspend fun getFavourites():List<Favourite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: Favourite)

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)
}