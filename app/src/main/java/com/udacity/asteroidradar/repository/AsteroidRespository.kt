package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Database.AsteroidDatabase
import com.udacity.asteroidradar.ImageOfTheDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseImageOfTheDayJsonResult
import com.udacity.asteroidradar.main.AsteroidFilter
import com.udacity.asteroidradar.main.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

// database is the single source of truth. This repository will do the query to the internet api then update the database.
// if the phone is off line some error may occur however the data can still be pulled from the database.
class AsteroidRepository(private val database: AsteroidDatabase) {

    suspend fun refreshAsteroidData(apiKey: String, startDate: String, endDate: String) {
        try {
            val asteroidResponse = NasaApi.retrofitService.getAsteroids(apiKey, startDate, endDate)
            val asteroidData = parseAsteroidsJsonResult(JSONObject(asteroidResponse))
            Timber.i("Response Successful: retrieved ${asteroidData.count()} asteroids")
            database.asteroidDao.insertAll(*asteroidData.toTypedArray())
        } catch (e: Exception) {
            Timber.i("Response Failed: ${e.message}")
        }

    }

    suspend fun refreshImageOfTheDay(apiKey: String) {
        try {
            val imageOfTheDayResponse = NasaApi.retrofitService.getImageOfTheDay(apiKey)
            val iotdData = parseImageOfTheDayJsonResult(JSONObject(imageOfTheDayResponse))
            Timber.i("Response Successful")
            database.asteroidDao.insertImageOfTheDay(iotdData)
        } catch (e: Exception) {
            Timber.i("Response Failed: ${e.message}")
        }

    }

    fun getAsteroids(filter: AsteroidFilter): LiveData<List<Asteroid>> {
        val asteroidData: LiveData<List<Asteroid>> = when (filter) {
            AsteroidFilter.SHOW_WEEK -> database.asteroidDao.getAsteroidsTodayOnwards(
                getCurrentDate()
            )
            AsteroidFilter.SHOW_TODAY -> database.asteroidDao.getAsteroidsToday(getCurrentDate())
            else -> database.asteroidDao.getAll()
        }
        return asteroidData
    }
}