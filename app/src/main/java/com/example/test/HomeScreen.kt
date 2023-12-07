package com.example.test

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

open class HomeScreen: AppCompatActivity() {

    lateinit var newsBtn : LinearLayout
    lateinit var weatherBtn : LinearLayout
    lateinit var commonFrameLayout : FrameLayout
    lateinit var newimg : ImageView
    lateinit var newstxt : TextView
    lateinit var weatherimg : ImageView
    lateinit var weathertxt : TextView
    private var location_Manager: LocationManager? = null
    private val REQUESTLOCATION = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        newsBtn = findViewById(R.id.newsBtn);
        weatherBtn = findViewById(R.id.weatherBtn);
        newimg = findViewById(R.id.newimg);
        newstxt = findViewById(R.id.newstxt);
        weatherimg = findViewById(R.id.weatherimg);
        weathertxt = findViewById(R.id.weathertxt);
        commonFrameLayout = findViewById(R.id.commonFrameLayout);
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUESTLOCATION
        )
        newsBtn.setOnClickListener {
            val i = Intent(this@HomeScreen, NewsActivity::class.java)
            startActivity(i)

        }

        weatherBtn.setOnClickListener {
            val i = Intent(this@HomeScreen, WeatherActivity::class.java)
            startActivity(i)
        }
        location_Manager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (!location_Manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            EnableGPS()
        } else {
            locations
        }
    }
    private fun EnableGPS() {
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
    private val locations: Unit
        get() {
            if (ActivityCompat.checkSelfPermission(
                    this@HomeScreen, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this@HomeScreen, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUESTLOCATION
                )
            } else {
                val locationGPS =
                    location_Manager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (locationGPS != null) {
                    val lat = locationGPS.latitude
                    val longi = locationGPS.longitude
                } else {
//                    Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    fun setView(viewLayout: View?, txt: String) {
        commonFrameLayout.addView(viewLayout)
        if(txt.equals("Weather")){
            newstxt.setTextColor(getResources().getColor(R.color.popup_darkgrey));
            weathertxt.setTextColor(getResources().getColor(R.color.buttonbg));
            newimg.setColorFilter(getResources().getColor(R.color.popup_darkgrey));
            weatherimg.setColorFilter(getResources().getColor(R.color.buttonbg));
        }else{
            newstxt.setTextColor(getResources().getColor(R.color.buttonbg));
            weathertxt.setTextColor(getResources().getColor(R.color.popup_darkgrey));
            newimg.setColorFilter(getResources().getColor(R.color.buttonbg));
            weatherimg.setColorFilter(getResources().getColor(R.color.popup_darkgrey));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.status_bar_color)
        }
    }
    override fun onBackPressed() {
        finishAffinity()
        finish()
    }

}