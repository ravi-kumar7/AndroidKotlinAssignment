package com.example.androidkotlinassignment.utility

import android.content.Context
import android.net.ConnectivityManager
import com.example.androidkotlinassignment.R
import java.net.InetAddress


class NetworkUtility {
    companion object {

        /* a static method to check if the device is connect ( Only for Android API version less than 21)
        * @param context: application context
        * @return a boolean true/false
        */
        fun internetCheck(c: Context): Boolean {
            val connectivityManager =
                c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
                .isConnectedOrConnecting
        }

        /* a static method to check if the Application is able to connect to API server.
        * @param context: application context
        * @return a boolean true/false
        */
        fun isAPIAvailable(c: Context): Boolean {
            return try {
                val ipAddr: InetAddress = InetAddress.getByName(c.getString(R.string.api_server))
                //You can replace it with your name
                ipAddr.hostAddress != ""
            } catch (e: Exception) {
                false
            }
        }
    }


}