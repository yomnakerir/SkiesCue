package com.example.skiescue.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skiescue.model.WeatherResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var db:RoomDB
    lateinit var dao: FavouriteDao
    @Before
    fun initDB() {
        // initialize database
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDB::class.java
        ).
        allowMainThreadQueries().build()

        dao = db.favouriteDao()
    }

    @After
    fun close() {
        // close database
        db.close()
    }


    @Test
    fun getFavorites_insertFavoriteItems_countOfItemsSame() = runBlockingTest {
        // Given
        val data1 = Favourite(
            id = 1,
           latitude = 30.020847056268064,
            longitude =  31.1904858698064,
            city = "cairo"
        )
        dao.insertFavourite(data1)

        val data2 = Favourite(
            id = 2,
            latitude = 30.020847056268064,
            longitude =  31.1904858698064,
            city = "cairo"
        )
        dao.insertFavourite(data2)


        val data3 = Favourite(
            id = 3,
            latitude = 30.020847056268064,
            longitude =  31.1904858698064,
            city = "cairo"
        )
        dao.insertFavourite(data3)

        // When
        val results = dao.getFavourites().first()

        // Then
        MatcherAssert.assertThat(results.size, `is`(3))
    }


    @Test
    fun insertFavorite_insertSingleItem_returnItem() = runBlockingTest{
        // Given
        val data1 = Favourite(
            id = 1,
            latitude = 30.020847056268064,
            longitude =  31.1904858698064,
            city = "cairo"
        )
        // When
        dao.insertFavourite(data1)

        // Then
        val results = dao.getFavourites().first()
        MatcherAssert.assertThat(results[0], IsNull.notNullValue())

    }

    @Test
    fun deleteFavorite_deleteItem_checkIsNull() = runBlockingTest {
        // Given
        val data1 = Favourite(
            id = 1,
            latitude = 30.020847056268064,
            longitude =  31.1904858698064,
            city = "cairo"
        )
        dao.insertFavourite(data1)
        // When
        dao.deleteFavourite(data1)
        // Then
        val results = dao.getFavourites().first()
        assertThat(results, IsEmptyCollection.empty())
        assertThat(results.size,`is`(0))
    }
}