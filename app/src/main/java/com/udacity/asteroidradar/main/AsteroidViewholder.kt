package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

class AsteroidViewHolder(private val binding: AsteroidListItemBinding): RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): AsteroidViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AsteroidListItemBinding.inflate(layoutInflater, parent, false)
            return AsteroidViewHolder(binding)
        }
    }

    fun bind(item: Asteroid, asteroidClickListener: AsteroidClickListener){
        binding.asteroid = item
        binding.clickListener = asteroidClickListener
    }
}