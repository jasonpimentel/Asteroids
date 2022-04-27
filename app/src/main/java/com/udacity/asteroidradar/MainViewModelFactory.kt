package com.udacity.asteroidradar

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.main.MainViewModel
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer

class MainViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java))
        {
            return MainViewModel(application) as T
        }

        throw IllegalArgumentException("unable to construct view Model")
    }
}