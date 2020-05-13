package com.example.androidkotlinassignment.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidkotlinassignment.R
import com.example.androidkotlinassignment.adapters.FactAdapter
import com.example.androidkotlinassignment.models.Fact
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
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        factsListView = view.findViewById(R.id.rv_facts_list)
        factsListView.layoutManager = LinearLayoutManager(context)
        factsListView.setHasFixedSize(true)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view)
        swipeRefreshLayout.setOnRefreshListener(this)

        progressBar = view.findViewById(R.id.progress_bar)
        rlProgressView = view.findViewById(R.id.rl_progress_view)
        loadingText = view.findViewById(R.id.tv_loading_text)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            } else {
                factsListView.visibility = View.VISIBLE
                factsList = it
                factAdapter = FactAdapter(factsList)
                factsListView.adapter = factAdapter
            }
        })
        viewModel.getStatusMsg().observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
        })
    }

    override fun onRefresh() {
        Toast.makeText(
            context,
            requireContext().getString(R.string.syncingData),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.syncDataFromAPI(true)
    }


}
