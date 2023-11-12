package com.driuft.random_pets_starter

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.driuft.weatherapp.WeatherAdapter
import okhttp3.Headers
import org.json.JSONObject
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class MainActivity : AppCompatActivity() {
    private lateinit var weatherList: MutableList<Weather>
    private lateinit var rvWeather: RecyclerView
    lateinit var weatherJson: JSONObject
    var weatherCity: String = ""
    var weatherIcon = ""
    var weatherTemperature = 0.0
    var weatherTempMax = 0.0
    var weatherTempMin = 0.0
    var weatherTime = ""
    var weatherType = ""


    data class Weather(val time: String, val temperature: Double, val icon: String)

    private fun getWeatherData(location: TextView, temperature: TextView, type: TextView, icon: ImageView, mmtemp: TextView) {
        val client = AsyncHttpClient()
        val cityID = "5128581"
        val apiKey = "7dc1b7f05e79b0e2e9bb55d8e7a84e83" // Your API Key

        client["https://api.openweathermap.org/data/2.5/forecast?id=$cityID&appid=$apiKey&units=metric", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("Weather Success", "$json")

                weatherCity = "New York"
                weatherJson = json.jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main")
                weatherTempMax = weatherJson.getDouble("temp_max")
                weatherTempMin = weatherJson.getDouble("temp_min")
                weatherTemperature = weatherJson.getDouble("temp")
                weatherTime = json.jsonObject.getJSONArray("list").getJSONObject(0).getString("dt_txt")
                weatherType = json.jsonObject.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main")
                weatherIcon = json.jsonObject.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon")
                val weatherTempCombined = "$weatherTempMax°/$weatherTempMin°"


                location.text = weatherCity
                temperature.text = weatherTemperature.toString()
                type.text = weatherType
                mmtemp.text = weatherTempCombined
                Glide.with(icon)
                    .load("https://openweathermap.org/img/wn/${weatherIcon}.png")
                    .fitCenter()
                    .into(icon)
                for (i in 0 until 10){
                    weatherJson = json.jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main")
                    weatherTime = json.jsonObject.getJSONArray("list").getJSONObject(i).getString("dt_txt").removeRange(0, 11).removeRange(5, 8)
                    weatherTemperature = weatherJson.getDouble("temp")
                    weatherIcon = json.jsonObject.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
                    val weather = Weather(weatherTime, weatherTemperature, weatherIcon)
                    weatherList.add(weather)

                    val adapter = WeatherAdapter(weatherList)
                    rvWeather.adapter = adapter
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Weather Error", errorResponse)
            }
        }]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cityLocation = findViewById<TextView>(R.id.cityLocation)
        val currentTemp = findViewById<TextView>(R.id.currentTemp)
        val currentWeather = findViewById<TextView>(R.id.currentWeather)
        val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
        val minmaxTemp = findViewById<TextView>(R.id.mmTemp)


        rvWeather = findViewById(R.id.weather_list)
        weatherList = mutableListOf()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvWeather.layoutManager = layoutManager

        getWeatherData(cityLocation, currentTemp, currentWeather, weatherIcon, minmaxTemp)



    }

}