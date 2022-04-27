package com.udacity.asteroidradar.Database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.ImageOfTheDay

@Dao
interface AsteroidDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: Asteroid)

    @Query("select * from asteroid_table order by close_approach_date asc")
    fun getAll():LiveData<List<Asteroid>>

    @Query("select * from asteroid_table where close_approach_date >= :today order by close_approach_date asc")
    fun getAsteroidsTodayOnwards(today: String): LiveData<List<Asteroid>>

    @Query("select * from asteroid_table where close_approach_date = :today")
    fun getAsteroidsToday(today: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageOfTheDay(imageOfTheDay: ImageOfTheDay)

    @Query("select * from image_of_the_day_table order by date desc Limit 1")
    fun getImageOfTheDay():LiveData<ImageOfTheDay>

    @Query("select * from asteroid_table where close_approach_date >= :startDate OR  close_approach_date <= :endDate order by close_approach_date desc")
    fun getWeeksAsteroids(startDate: String, endDate: String): LiveData<List<Asteroid>>
}