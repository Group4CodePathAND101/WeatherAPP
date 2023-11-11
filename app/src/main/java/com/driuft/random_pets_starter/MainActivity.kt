package com.driuft.random_pets_starter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.driuft.weatherapp.WeatherAdapter
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var weatherList: MutableList<Weather>
    private lateinit var rvWeather: RecyclerView

    data class Weather(val name: String, val temperature: Double, val humidity: Int, val icon: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvWeather = findViewById(R.id.weather_list)
        weatherList = mutableListOf()

        val layoutManager = LinearLayoutManager(this)
        rvWeather.layoutManager = layoutManager

        // Replace with actual city IDs or coordinates
        val cityIds = listOf("524901", "703448", "2643743") // Example city IDs
        cityIds.forEach { id ->
            getWeatherData(id)
        }
    }

    private fun getWeatherData(cityId: String) {
        val client = AsyncHttpClient()
        val apiKey = "7dc1b7f05e79b0e2e9bb55d8e7a84e83" // Your API Key
        val url = "https://api.openweathermap.org/data/2.5/weather?id=$cityId&appid=$apiKey&units=metric"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Weather Success", "$json")

                val weatherJson = json.jsonObject.getJSONObject("main")
                val weatherName = json.jsonObject.getString("name")
                val weatherTemperature = weatherJson.getDouble("temp")
                val weatherHumidity = weatherJson.getInt("humidity")
                val weatherIcon = json.jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon")

                val weather = Weather(weatherName, weatherTemperature, weatherHumidity, weatherIcon)
                weatherList.add(weather)

                val adapter = WeatherAdapter(weatherList)
                rvWeather.adapter = adapter
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Weather Error", errorResponse)
            }
        })
    }
}