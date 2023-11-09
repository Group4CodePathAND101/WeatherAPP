package com.driuft.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.driuft.random_pets_starter.R
import com.driuft.weatherapp.MainActivity.Weather

class WeatherAdapter(private val weatherList: List<Weather>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.weather_name)
        val temperatureTextView: TextView = itemView.findViewById(R.id.weather_temperature)
        val humidityTextView: TextView = itemView.findViewById(R.id.weather_humidity)
        val iconImageView: ImageView = itemView.findViewById(R.id.weather_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val weatherView = inflater.inflate(R.layout.item_weather, parent, false)

        return ViewHolder(weatherView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.nameTextView.text = weather.name
        holder.temperatureTextView.text = "${weather.temperature}°C"
        holder.humidityTextView.text = "Humidity: ${weather.humidity}%"

        // Assuming you have a valid url for the weather icon
        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${weather.icon}.png")
            .into(holder.iconImageView)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}