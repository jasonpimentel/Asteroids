package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "image_of_the_day_table")
data class ImageOfTheDay(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(name ="date")
    val date: String,
    @ColumnInfo(name ="explanation")
    val explanation: String,
    @ColumnInfo(name ="hdUrl")
    val hdUrl: String,
    @ColumnInfo(name ="media_type")
    val media_type: String,
    @ColumnInfo(name ="service_version")
    val service_version: String,
    @ColumnInfo(name ="title")
    val title: String,
    @ColumnInfo(name ="url")
    val url: String
): Parcelable