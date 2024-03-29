package com.example.skiescue.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.skiescue.alert.view.AlertDao
import com.example.skiescue.alert.view.AlertModel


@Database(entities = [Favourite::class, AlertModel::class], version = 16, exportSchema = false)
@TypeConverters(Converter::class)
abstract class RoomDB:RoomDatabase(){
    abstract fun favouriteDao():FavouriteDao
    abstract fun alertDao():AlertDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RoomDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE?: synchronized(LOCK){
            INSTANCE ?: createDatabase(context).also{INSTANCE = it}
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                RoomDB::class.java,
                name = "favourite.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}