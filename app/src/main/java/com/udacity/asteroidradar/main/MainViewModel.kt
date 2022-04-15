package com.udacity.asteroidradar.main

import android.app.Application
import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.ImageOfTheDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseImageOfTheDayJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

private const val apiKey = "yv0JbswidOGtrb0Pvp8weF9xmyDefpXlWKO6rnLJ"

class MainViewModel() : ViewModel() {

    private val _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroids: LiveData<List<Asteroid>>
    get() = _asteroids

    private val _response = MutableLiveData<String>()

    private val _imageOfTheDay = MutableLiveData<ImageOfTheDay>()

    val imageOfTheDay: LiveData<ImageOfTheDay>
    get() = _imageOfTheDay

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()
    val navigateToAsteroid: LiveData<Asteroid>
    get() = _navigateToAsteroid

    init {
        getAsteroids()
        getImageOfTheDay()
    }

    fun doneNavigatingToAsteroid(){
        _navigateToAsteroid.value = null
    }

    fun onNavigateToAsteroid(asteroid: Asteroid){
        _navigateToAsteroid.value = asteroid
    }

    private fun getAsteroids(){
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                val startDate = dateFormat.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, 7)
                val endDate = dateFormat.format(calendar.time)
                val asteroidResponse =  NasaApi.retrofitService.getAsteroids(apiKey, startDate, endDate)
                val asteroidData = parseAsteroidsJsonResult(JSONObject(asteroidResponse))
                _asteroids.value = asteroidData.toList()
                _response.value = "Success ${asteroids.value?.size} asteroids found"

            } catch (e: Exception){
                _response.value = "Failure ${e.message}"
                Timber.i(_response.value)
            }
        }
    }

    private fun getImageOfTheDay(){
        viewModelScope.launch {
            try {
                val imageOfTheDayResponse = NasaApi.retrofitService.getImageOfTheDay(apiKey)
                val imageOfTheDayData = parseImageOfTheDayJsonResult(JSONObject(imageOfTheDayResponse))
                _imageOfTheDay.value = imageOfTheDayData
                Timber.i("Success image_url: ${imageOfTheDayData.url}")
            } catch (e: Exception){
                Timber.i("Failure to get image error: ${e.message}")
            }
        }
    }
}