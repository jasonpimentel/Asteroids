package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Database.getDatabase
import com.udacity.asteroidradar.main.getCurrentDate
import com.udacity.asteroidradar.main.getOffsetDate
import com.udacity.asteroidradar.repository.AsteroidRepository

private const val apiKey = "yv0JbswidOGtrb0Pvp8weF9xmyDefpXlWKO6rnLJ"

class RefreshDataWorker(private val appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object{
        const val WORK_NAME="RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val database = getDatabase(appContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshAsteroidData(apiKey, getCurrentDate(), getOffsetDate(7))
            Result.success()
        } catch (e: Exception){
            Result.retry()
        }
    }
}