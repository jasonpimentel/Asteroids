package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Database.getDatabase
import com.udacity.asteroidradar.ImageOfTheDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private const val apiKey = "yv0JbswidOGtrb0Pvp8weF9xmyDefpXlWKO6rnLJ"

enum class AsteroidFilter { SHOW_TODAY, SHOW_SAVED, SHOW_WEEK }

class MainViewModel(private val application: Application) : ViewModel() {

    private val database = getDatabase(application)

    private val _asteroidFilter = MutableLiveData<AsteroidFilter>()
    val asteroidFilter: LiveData<AsteroidFilter>
    get() = _asteroidFilter

    val asteroids = Transformations.switchMap(asteroidFilter){
        repository.getAsteroids(it)
    }

    val imageOfTheDay = database.asteroidDao.getImageOfTheDay()

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid: LiveData<Asteroid?>
        get() = _navigateToAsteroid

    private val repository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                refreshAsteroidData()
                refreshImageOfTheDay()
            }
            updateFilter(AsteroidFilter.SHOW_WEEK)
        }
    }

    fun doneNavigatingToAsteroid() {
        _navigateToAsteroid.value = null
    }

    fun onNavigateToAsteroid(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun updateFilter(filter: AsteroidFilter) {
        _asteroidFilter.value = filter
    }

    private suspend fun refreshAsteroidData() {
        val startTime = getCurrentDate()
        val endTime = getOffsetDate(7)
        repository.refreshAsteroidData(apiKey, startTime, endTime)
    }

    private suspend fun refreshImageOfTheDay() {
        repository.refreshImageOfTheDay(apiKey)
    }
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime)
}

fun getOffsetDate(days: Int): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    calendar.add(Calendar.DAY_OF_YEAR, days)
    return dateFormat.format(calendar.time)
}

