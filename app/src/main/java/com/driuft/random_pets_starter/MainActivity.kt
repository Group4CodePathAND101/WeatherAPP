package com.driuft.random_pets_starter


import android.Manifest
import java.time.LocalDateTime
import java.util.Calendar
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.os.Build
import com.google.android.gms.location.LocationRequest
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.bumptech.glide.Glide
import okhttp3.Headers
import org.json.JSONObject
import com.driuft.weatherapp.WeatherAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val REQUEST_LOCATION_PERMISSION = 1001
    private lateinit var weatherList: MutableList<Weather>
    private lateinit var rvWeather: RecyclerView
    lateinit var weatherJson: JSONObject
    var weatherIcon = ""
    var weatherTemperature = 0.0
    var weatherTempMax = 0.0
    var weatherTempMin = 0.0
    var weatherTime = ""
    var weatherType = ""

    private var shouldFetchCurrentLocationWeather = true




    data class Weather(val time: String, val temperature: Double, val icon: String)


    data class City(val name: String, val latitude: Double, val longitude: Double)

    fun getDrawableResourceForWeatherIcon(iconCode: String): Int? {
        return when (iconCode) {
            "01d" -> R.drawable.day_clear
            "01n" -> R.drawable.night_full_moon_clear
            "02d" -> R.drawable.day_partial_cloud
            "02n" -> R.drawable.night_full_moon_partial_cloud
            "03d" -> R.drawable.cloudy
            "03n" ->R.drawable.night_half_moon_partial_cloud
            "04d" -> R.drawable.angry_clouds
            "04n" -> R.drawable.night_full_moon_sleet
            "09d" -> R.drawable.rain
            "09n" -> R.drawable.rain
            "10d" -> R.drawable.day_rain
            "10n" ->R.drawable.night_half_moon_rain
            "11d" -> R.drawable.day_rain_thunder
            "11n" -> R.drawable.night_full_moon_rain_thunder
            "13d" -> R.drawable.snow
            "13n" ->R.drawable.snow


            // ... map other icon codes ...
            else -> null // A default icon
        }
    }

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
            )else {
                // Request location permissions since they are not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        // Make the weather API request with latitude and longitude
                        val currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric"
                        client.get(
                            currentWeatherUrl,
                            object : JsonHttpResponseHandler() {
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                                    Log.d("Weather Success", "$json")

                                    val dateView = findViewById<TextView>(R.id.currentDate)

                                    val calendar = Calendar.getInstance()
                                    val formatter = SimpleDateFormat("EEE, d, MMMM HH:mm", Locale.getDefault())
                                    val formattedDateTime = formatter.format(calendar.time)

                                    dateView.text = formattedDateTime


                                    if (json.jsonObject.has("name")) {
                                        // If the key exists, then retrieve its value
                                        val cityName = json.jsonObject.getString("name")
                                        Log.d("City Name", cityName)

                                        weatherJson = json.jsonObject.getJSONObject("main")
                                        weatherTempMax = weatherJson.getDouble("temp_max")
                                        weatherTempMin = weatherJson.getDouble("temp_min")
                                        weatherTemperature = String.format("%.1f", weatherJson.getDouble("temp")).toDouble()
                                        weatherType = json.jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
                                        weatherIcon = json.jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon")

                                        val maxTempFormatted = String.format("%.1f°C", weatherTempMax)
                                        val minTempFormatted = String.format("%.1f°C", weatherTempMin)

                                        val drawableId = getDrawableResourceForWeatherIcon(weatherIcon)
                                        val weatherTempCombined = "$maxTempFormatted/ $minTempFormatted"



                                        cityLocation.text = cityName
                                        temperature.text = weatherTemperature.toString()
                                        type.text = weatherType
                                        mmtemp.text = weatherTempCombined
                                        drawableId?.let {
                                            icon.setImageResource(it)
                                        } ?: kotlin.run {
                                            Glide.with(icon)
                                                .load("https://openweathermap.org/img/wn/${weatherIcon}.png")
                                                .fitCenter()
                                                .into(icon)
                                        }

                                        fetchForecastData(client,latitude,longitude,apiKey)

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

    private fun fetchForecastData(client: AsyncHttpClient, latitude: Double, longitude: Double, apiKey: String) {
        val forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric"

        client.get(forecastUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                // Process forecast data
                weatherJson = json.jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main")
                weatherTempMax = weatherJson.getDouble("temp_max")
                weatherTempMin = weatherJson.getDouble("temp_min")
                weatherTemperature = weatherJson.getDouble("temp")
                weatherTime = json.jsonObject.getJSONArray("list").getJSONObject(0).getString("dt_txt")
                weatherType = json.jsonObject.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main")
                weatherIcon = json.jsonObject.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon")


                for (i in 0 until 10) {
                    weatherJson = json.jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main")
                    weatherTime = json.jsonObject.getJSONArray("list").getJSONObject(i).getString("dt_txt").removeRange(0, 11).removeRange(5, 8)
                    weatherTemperature = String.format("%.1f",weatherJson.getDouble("temp")).toDouble()
                    weatherIcon = json.jsonObject.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
                    val weather = Weather(weatherTime, weatherTemperature, weatherIcon)
                    weatherList.add(weather)

                    val adapter = WeatherAdapter(weatherList)
                    rvWeather.adapter = adapter
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.d("Forecast Error", "Forecast fetch failed: $errorResponse")
            }
        })
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val cityLocation = findViewById<TextView>(R.id.cityLocation)
        val currentTemp = findViewById<TextView>(R.id.currentTemp)
        val currentWeather = findViewById<TextView>(R.id.currentWeather)
        val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
        val minmaxTemp = findViewById<TextView>(R.id.mmTemp)
        val date = findViewById<TextView>(R.id.currentDate)

        val refreshButton = findViewById<Button>(R.id.refreshButton)
        refreshButton.setOnClickListener {
            resumeLocationUpdates()
        }



        val btnRandomLocation = findViewById<Button>(R.id.btnRandomLocation)
        btnRandomLocation.setOnClickListener {
            loadRandomLocationWeather()
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // Use the query to fetch and display the weather data for the specified city
                    searchForCityWeather(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle changes in the search query if needed
                return false
            }
        })


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
                if (shouldFetchCurrentLocationWeather) {
                    locationResult?.lastLocation?.let { location ->
                        // Update the UI with the weather data for the current location
                        getWeatherData(cityLocation, currentTemp, currentWeather, weatherIcon, minmaxTemp)
                    }
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
    private fun searchForCityWeather(cityName: String) {
        val apiKey = "7dc1b7f05e79b0e2e9bb55d8e7a84e83" // Replace with your actual API key
        val weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$apiKey&units=metric"

        // Clear current weather information from the UI
        shouldFetchCurrentLocationWeather= false
        clearCurrentWeatherUI()
        stopLocationUpdates()

        // Initialize the client to perform network requests
        val client = AsyncHttpClient()

        // Make the network request for the entered city
        client.get(weatherUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, response: JSON) {
                // This is where you parse the response and update the UI accordingly
                this@MainActivity.runOnUiThread {
                    // Parse the weather information from the response
                    val weatherJson = response.jsonObject
                    val mainJson = weatherJson.getJSONObject("main")

                    val temperature = mainJson.getDouble("temp")
                    val weatherDescription = weatherJson.getJSONArray("weather").getJSONObject(0).getString("main")
                    val iconCode = weatherJson.getJSONArray("weather").getJSONObject(0).getString("icon")

                    // Update the UI with the fetched data
                    findViewById<TextView>(R.id.cityLocation).text = cityName
                    findViewById<TextView>(R.id.currentTemp).text = "$temperature°C"
                    findViewById<TextView>(R.id.currentWeather).text = weatherDescription
                    val iconImageView = findViewById<ImageView>(R.id.weatherIcon)

                    // Load the weather icon
                    val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"
                    Glide.with(this@MainActivity).load(iconUrl).into(iconImageView)

                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                // Handle error - could not retrieve weather data
                Log.e("WeatherError", "Failed to fetch weather data: $errorResponse")
            }
        })
    }

    private fun clearCurrentWeatherUI() {
        // Clear all weather information from the UI components
        findViewById<TextView>(R.id.cityLocation).text = ""
        findViewById<TextView>(R.id.currentTemp).text = ""
        findViewById<TextView>(R.id.currentWeather).text = ""
        val iconImageView = findViewById<ImageView>(R.id.weatherIcon)
        iconImageView.setImageResource(android.R.color.transparent) // or set to a default image
    }

    private fun loadRandomLocationWeather() {
        val cities = listOf(
            // Replace these with actual latitude and longitude values
            City("New York", 40.712776, -74.005974),
            City("Tokyo", 35.689487, 139.691711),
            City("London", 51.507351, -0.127758),
            City("Paris", 48.856613, 2.352222) ,
            City("Los Angeles", 34.052235, -118.243683),
            City("Beijing", 39.904200, 116.407396),
            City("Rome", 41.902783, 12.496366),
            City("Sydney", -33.865143, 151.209900),
            City("Cairo", 30.044420, 31.235712),
            City("Mumbai", 19.076090, 72.877426),
            City("Mexico City", 19.432608, -99.133209),
            City("Istanbul", 41.008238, 28.978359),
            City("Sao Paulo", -23.550520, -46.633308)


        )
        stopLocationUpdates()

        val randomCity = cities.random()
        searchForCityWeather(randomCity.name)
    }

    private fun resumeLocationUpdates() {
        if (hasLocationPermission()) {
            // Allow location updates to change the weather info
            shouldFetchCurrentLocationWeather = true

            // Restart location updates
            startLocationUpdates()
        } else {
            // Request location permissions since they are not granted
            requestLocationPermission()
        }
    }

}