package com.example.androidkotlinassignment.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidkotlinassignment.R
import com.example.androidkotlinassignment.adapters.FactAdapter
import com.example.androidkotlinassignment.viewmodels.MainViewModel
import com.example.androidkotlinassignment.models.Fact

class FactsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance() = FactsFragment()
    }

    private lateinit var factAdapter: FactAdapter
    private lateinit var factsListView: RecyclerView
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
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onRefresh() {

    }


}
