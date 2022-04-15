package com.udacity.asteroidradar

data class ImageOfTheDay(
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)