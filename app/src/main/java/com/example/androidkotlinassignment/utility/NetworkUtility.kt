package com.example.androidkotlinassignment.utility

import android.content.Context
import com.example.androidkotlinassignment.R

class NetworkUtility {
    companion object {

        /* a static method to check API server connectivity
        * @param context: application context
        * @return a boolean true/false
        */
        fun internetCheck(c: Context): Boolean {
            val command = "ping -c 1 ${c.getString(R.string.api_server)}"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        }
    }


}