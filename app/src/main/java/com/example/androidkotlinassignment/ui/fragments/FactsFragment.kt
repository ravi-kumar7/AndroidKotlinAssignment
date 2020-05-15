package com.example.androidkotlinassignment.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidkotlinassignment.MyApplication
import com.example.androidkotlinassignment.R
import com.example.androidkotlinassignment.adapters.FactAdapter
import com.example.androidkotlinassignment.databinding.FactsFragmentBinding
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.utility.NetworkUtility
import com.example.androidkotlinassignment.viewmodels.MainViewModel

class FactsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance() = FactsFragment()
    }

    private lateinit var factAdapter: FactAdapter
    private lateinit var factsListView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView
    private lateinit var rlProgressView: RelativeLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var factsList: List<Fact>

    private val viewModel by viewModels<MainViewModel>() {
        MainViewModel.FactsViewModelFactory((requireContext().applicationContext as MyApplication).factsDataRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factsFragmentBinding = FactsFragmentBinding.inflate(inflater, container, false)
        factsListView = factsFragmentBinding.rvFactsList
        factsListView.layoutManager = LinearLayoutManager(context)
        factsListView.setHasFixedSize(true)
        swipeRefreshLayout = factsFragmentBinding.swipeRefreshView
        swipeRefreshLayout.setOnRefreshListener(this)

        progressBar = factsFragmentBinding.progressBar
        rlProgressView = factsFragmentBinding.rlProgressView
        loadingText = factsFragmentBinding.tvLoadingText
        return factsFragmentBinding.root
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Creating observers for live data
        viewModel.getFactCategory().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = false
            if (it != null)
                requireActivity().title = it.title
        })
        viewModel.getFacts().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                loadingText.text = requireContext().getString(R.string.no_data)
                progressBar.visibility = View.INVISIBLE
                rlProgressView.visibility = View.VISIBLE
                factsListView.visibility = View.GONE
            } else {
                rlProgressView.visibility = View.GONE
                factsListView.visibility = View.VISIBLE
                factsList = it
                factAdapter = FactAdapter(factsList)
                factsListView.adapter = factAdapter
            }
        })
        viewModel.getStatusMsg().observe(viewLifecycleOwner, Observer {
            if (it.isNotBlank()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
            viewModel.setStatusMsg("")

        })

        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
            NetworkUtility.internetCheck(requireContext()) -> {
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
            Toast.makeText(requireContext(), getString(R.string.syncingData), Toast.LENGTH_LONG)
                .show()
            viewModel.syncDataFromAPI()
        }
    }

    @ExperimentalStdlibApi
    override fun onRefresh() {
        Toast.makeText(
            context,
            requireContext().getString(R.string.syncingData),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.syncDataFromAPI(true)
    }


}
