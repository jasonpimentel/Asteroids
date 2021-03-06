package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "asteroid_table")
data class Asteroid(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(name ="code_name")
    val codename: String,
    @ColumnInfo(name ="close_approach_date")
    val closeApproachDate: String,
    @ColumnInfo(name ="absolute_magnitude")
    val absoluteMagnitude: Double,
    @ColumnInfo(name ="estimate_Diameter")
    val estimatedDiameter: Double,
    @ColumnInfo(name ="relative_velocity")
    val relativeVelocity: Double,
    @ColumnInfo(name ="distance_from_earth")
    val distanceFromEarth: Double,
    @ColumnInfo(name ="is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean
) : Parcelable