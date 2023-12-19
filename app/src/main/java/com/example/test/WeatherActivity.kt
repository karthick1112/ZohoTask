package com.example.test

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.test.ModelClass.GetWeatherListResponseBody
import com.example.test.NetworkUtil.NetworkUtil
import com.example.test.Retrofit.ApiClient
import com.example.test.databinding.WeatherLayoutBinding
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.sql.Date
import java.text.SimpleDateFormat


class WeatherActivity : HomeScreen() {

    var idPBLoading: ProgressBar?=null

    var locationName: TextView?=null
    var degree: TextView?=null
    var weather: TextView?=null
    var temprature: TextView?=null
    var pressure: TextView?=null
    var humidity: TextView?=null
    var feelsLike: TextView?=null
    var visibility: TextView?=null
    var sunrisetime: TextView?=null
    var sunsettime: TextView?=null

    var bck: LinearLayout?=null

    private var locationManager: LocationManager? = null
    private var latitude = ""
    private var longitude = ""
    lateinit var weatherlayout: WeatherLayoutBinding
    var subscriptions: Subscription? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherlayout = WeatherLayoutBinding.inflate(layoutInflater)
        setView(weatherlayout.getRoot(), "Weather")

        idPBLoading = findViewById(R.id.idPBLoading)
        locationName = findViewById(R.id.locationName)
        degree = findViewById(R.id.degree)
        weather = findViewById(R.id.weather)
        temprature = findViewById(R.id.temprature)
        pressure = findViewById(R.id.pressure)
        humidity = findViewById(R.id.humidity)
        feelsLike = findViewById(R.id.feelsLike)
        visibility = findViewById(R.id.visibility)
        sunrisetime = findViewById(R.id.sunrisetime)
        sunsettime = findViewById(R.id.sunsettime)
        bck = findViewById(R.id.bck)

        bck!!.setOnClickListener {
            onBackPressed()
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION
        )

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (!locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            onGPS()
        } else {
            location
        }

    }

    private fun onGPS() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Enable GPS").setCancelable(false)
            .setPositiveButton("Yes") { dialog, which ->
                startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            }
            .setNegativeButton("No") { dialog, which -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private val location: Unit
        get() {
            if (ActivityCompat.checkSelfPermission(
                    this@WeatherActivity, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this@WeatherActivity, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION
                )
            } else {
                if(locationManager != null){
                    val locationGPS =
                        locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (locationGPS != null) {
                        val lat = locationGPS.latitude
                        val longi = locationGPS.longitude
                        latitude = lat.toString()
                        longitude = longi.toString()
                        getWeatherListApi()
                    } else {
                    }
                }

            }
        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (ActivityCompat.checkSelfPermission(
                    this@WeatherActivity, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this@WeatherActivity, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                location
            }
        }

    }

    companion object {
        private const val REQUEST_LOCATION = 1
    }

    private fun getWeatherListApi() {
        if (NetworkUtil.checkActiveInternetConnection(this@WeatherActivity)) {
            idPBLoading!!.visibility = View.VISIBLE
            subscriptions = ApiClient.apiServiceweather.getWeatherLists(latitude, longitude, "9f507d5ab1dc297379fe3fafa9a45ca3")
                .observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(object : Observer<GetWeatherListResponseBody> {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        Log.e("Profile Detail", e.toString())
                    }

                    @SuppressLint("SuspiciousIndentation")
                    override fun onNext(response: GetWeatherListResponseBody) {
                        idPBLoading!!.visibility = View.GONE

                            if(response != null){
                                idPBLoading!!.visibility = View.GONE

                                if(response.name!=null){
                                    locationName!!.text = response.name
                                } else {
                                    locationName!!.text = ""
                                }

                                if(response.main!!.temp!=null){
                                    degree!!.text = KelvinToCelsius(response.main!!.temp!!).toString()+getString(R.string.degree_celsius)
                                } else {
                                    degree!!.text = ""
                                }

                                if(response.weather != null && response.weather!!.size > 0){
                                    for (i in 0..response.weather!!.size-1){
                                        if(response.weather!!.get(i).description != null) {
                                            weather!!.text = response.weather!!.get(i).description
                                        } else {
                                            weather!!.text = ""
                                        }

                                    }
                                }
                                if(response.sys != null){
                                    if(response!!.sys!!.sunrise != null){
                                        sunrisetime!!.text = getTimefromTimestamp(response!!.sys!!.sunrise!!.toLong())
                                    }
                                    if(response!!.sys!!.sunset != null){
                                        sunsettime!!.text = getTimefromTimestamp(response!!.sys!!.sunset!!.toLong())
                                    }
                                }
                                if (response.main != null) {
                                    temprature!!.text = "High: ${response.main!!.tempMax}" + "       Low: ${response.main!!.tempMin}"
                                    pressure!!.text = response.main!!.pressure.toString()+" hPa"
                                    humidity!!.text = response.main!!.humidity.toString() +" %"
                                    feelsLike!!.text = KelvinToCelsius(response.main!!.feelsLike!!).toString()+getString(R.string.degree_celsius)
                                } else {
                                    temprature!!.text = "High:   " + "  Low: "
                                    pressure!!.text = ""
                                    humidity!!.text = ""
                                    feelsLike!!.text = ""
                                }

                                if (response.visibility != null){
                                    visibility!!.text = response.visibility.toString() + " M"
                                }
                            } else {
                                idPBLoading!!.visibility = View.GONE
                            }
                    }

                })
        }else{
            Toast.makeText(this,getString(R.string.check_internet),Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTimefromTimestamp(sunrise: Long): String {
        val sdf = SimpleDateFormat("hh:mm a")
        val netDate = Date(sunrise * 1000)
        return sdf.format(netDate).toString()
    }

    fun KelvinToCelsius(celsius: Double): Int {
        val inches: Double
        inches = celsius - 273.15
        return inches.toInt()
    }
}