package com.example.androidkotlinassignment

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.androidkotlinassignment.ui.fragments.FactsFragment
import com.example.androidkotlinassignment.utility.NetworkUtility
import com.example.androidkotlinassignment.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FactsFragment.newInstance())
                .commitNow()
        }
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when {
            // Register network callback to monitor network changes.
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {

                val networkRequest = NetworkRequest.Builder().build()
                connectivityManager.registerNetworkCallback(networkRequest, object :
                    ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        syncDB()
                    }
                })
            }
            // To support Android versions below lollipop
            NetworkUtility.internetCheck(baseContext) -> {
                syncDB()
            }
            else -> {
                viewModel.setStatusMsg(getString(R.string.networkConnectivityError))
            }
        }
    }


    @ExperimentalStdlibApi
    fun syncDB() {
        if (!viewModel.isDataSynced().value!!) {
            Toast.makeText(this, getString(R.string.syncingData), Toast.LENGTH_LONG).show()
            viewModel.syncDataFromAPI()
        }
    }

}
