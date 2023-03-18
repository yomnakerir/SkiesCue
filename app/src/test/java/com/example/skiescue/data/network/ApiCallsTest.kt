package com.example.skiescue.data.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


import com.example.skiescue.model.WeatherResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ApiCallsTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var apiCalls: ApiCalls? = null

    @Before
    fun setUp() {
        apiCalls = RetrofitInstance().apiCall()
    }

    @After
    fun tearDown() {
        apiCalls = null
    }

    @Test
    fun getWeatherDetails_requestAPI_isSuccessful() = runBlocking {
        // Given
        val latitude = 25.122
        val longitude = 35.56
        val apiKey = "bec88e8dd2446515300a492c3862a10e"
        // When
        val response =  apiCalls?.getWeatherDetalis(
            lat= latitude,
            lon = longitude,
            appid = apiKey,
        )

        // Then
        assertThat(response?.code() as Int, Is.`is`(200))
        assertThat(response.body() as WeatherResponse, notNullValue())
        assertThat(response.errorBody() , nullValue())

    }

    @Test
    fun getWeatherDetails_requestAPINoKey_unAuthorized() = runBlocking {
        // Given
        val latitude = 25.122
        val longitude = 35.56
        val apiKey = ""
        // When
        val response =  apiCalls?.getWeatherDetalis(
            lat= latitude,
            lon = longitude,
            appid = apiKey,
        )

        // Then
        assertThat(response?.code() as Int, Is.`is`(401))
        assertThat(response.body() , nullValue())
        assertThat(response.errorBody() , notNullValue())

    }
}