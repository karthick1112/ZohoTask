package com.example.test.NetworkUtil

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder

object NetworkUtil {
    var TYPE_WIFI = 1
    var TYPE_MOBILE = 2
    var TYPE_NOT_CONNECTED = 0
    private fun getConnectivityStatus(context: Context): Int {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    fun canGetLocation(context: Context): Boolean {
        var result = true
        var lm: LocationManager? = null
        var gps_enabled = false
        var network_enabled = false
        if (lm == null) lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            network_enabled = lm
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        result = gps_enabled != false && network_enabled != false
        return result
    }

    fun getConnectivityStatusBoolean(context: Context): Boolean {
        val conn = getConnectivityStatus(context)
        if (conn == TYPE_WIFI) {
            return true
        } else if (conn == TYPE_MOBILE) {
            return true
        } else if (conn == TYPE_NOT_CONNECTED) {
            return false
        }
        return false
    }

    fun checkActiveInternetConnection(context: Context): Boolean {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        // Network is present and connected
        return networkInfo != null && networkInfo.isConnected
    }

    fun getAddressFromLocation(latitude: Double, longitude: Double, context: Context): String {

        // Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        val geocoder = Geocoder(context, context.resources.configuration.locale)
        var result = ""
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)) //.append("\n");
                }
                sb.append(address.locality).append("\n")
                //sb.append(address.getPostalCode()).append("\n");
                //sb.append(address.getCountryName());
                result = sb.toString()
                return result
            }
        } catch (e: IOException) {
            //Log.e("show", "Unable connect to Geocoder", e);
        } finally {
        }
        return result
    }
}