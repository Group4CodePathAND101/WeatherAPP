package com.driuft.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.driuft.weatherapp.R

class WeatherAdapter(private val weatherList: List<MainActivity.Weather>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weatherIcon: ImageView = view.findViewById(R.id.weather_icon)
        val weatherName: TextView = view.findViewById(R.id.weather_name)
        val weatherStats: TextView = view.findViewById(R.id.weather_stats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeather = weatherList[position]

        // Load weather icon from OpenWeatherMap icon URL
        val iconUrl = "http://openweathermap.org/img/w/${currentWeather.icon}.png"
        Glide.with(holder.itemView)
            .load(iconUrl)
            .into(holder.weatherIcon)

        holder.weatherName.text = currentWeather.name
        holder.weatherStats.text = "Temp: ${currentWeather.temperature}°C, Humidity: ${currentWeather.humidity}%"
    }

    override fun getItemCount() = weatherList.size
}