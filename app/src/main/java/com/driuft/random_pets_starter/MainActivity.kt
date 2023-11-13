package com.driuft.random_pets_starter


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.LocationRequest
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import okhttp3.Headers
import org.json.JSONObject
import com.driuft.weatherapp.WeatherAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val REQUEST_LOCATION_PERMISSION = 1001
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

    private fun hasLocationPermission():Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }



    private fun getWeatherData(cityLocation: TextView, temperature: TextView, type: TextView, icon: ImageView, mmtemp: TextView) {
        val client = AsyncHttpClient()
        val apiKey = "7dc1b7f05e79b0e2e9bb55d8e7a84e83" // Your API Key
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        // Make the weather API request with latitude and longitude
                        client.get(
                            "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric",
                            object : JsonHttpResponseHandler() {
                                override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                                    Log.d("Weather Success", "$json")

                                    if (json.jsonObject.has("name")) {
                                        // If the key exists, then retrieve its value
                                        val cityName = json.jsonObject.getString("name")
                                        Log.d("City Name", cityName)


                                        weatherJson = json.jsonObject.getJSONObject("main")
                                        weatherTempMax = weatherJson.getDouble("temp_max")
                                        weatherTempMin = weatherJson.getDouble("temp_min")
                                        weatherTemperature = weatherJson.getDouble("temp")
                                        weatherType = json.jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
                                        weatherIcon = json.jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon")
                                        val weatherTempCombined =
                                            "$weatherTempMax°/$weatherTempMin°"

                                        cityLocation.text = cityName
                                        temperature.text = weatherTemperature.toString()
                                        type.text = weatherType
                                        mmtemp.text = weatherTempCombined
                                        Glide.with(icon)
                                            .load("https://openweathermap.org/img/wn/${weatherIcon}.png")
                                            .fitCenter()
                                            .into(icon)

//                                        for (i in 0 until 10) {
//                                            weatherJson = json.jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main")
//                                            weatherTime = json.jsonObject.getJSONArray("list").getJSONObject(i).getString("dt_txt").removeRange(0, 11).removeRange(5, 8)
//                                            weatherTemperature = weatherJson.getDouble("temp")
//                                            weatherIcon = json.jsonObject.getJSONArray("list")
//                                                .getJSONObject(i)
//                                                .getJSONArray("weather").getJSONObject(0)
//                                                .getString("icon")
//                                            val weather = Weather(weatherTime, weatherTemperature, weatherIcon)
//                                            weatherList.add(weather)
//
//                                            val adapter = WeatherAdapter(weatherList)
//                                            rvWeather.adapter = adapter
//                                        }
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
                            }
                        )
                    }
                }
        } else {
            requestLocationPermission()
        }
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation?.let { location ->
                    // Handle the received location data here
                    getWeatherData(cityLocation, currentTemp, currentWeather, weatherIcon, minmaxTemp)
                }
            }
        }

        if (hasLocationPermission()) {
            startLocationUpdates()
        } else {
            requestLocationPermission()
        }


        getWeatherData(cityLocation, currentTemp, currentWeather, weatherIcon, minmaxTemp)



    }

}