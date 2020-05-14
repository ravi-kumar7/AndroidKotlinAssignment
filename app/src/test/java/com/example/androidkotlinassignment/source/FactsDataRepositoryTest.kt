package com.example.androidkotlinassignment.source

import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.models.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class FactsDataRepositoryTest {
    private lateinit var factsDataRepository: FactsDataRepository
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var remoteDataSource: FakeRemoteDataSource

    private val factCategory: MutableLiveData<FactCategory> = MutableLiveData()
    private val allFacts: MutableLiveData<List<Fact>> = MutableLiveData()
    var isConnected = false
    var response: ArrayList<Model.Row> = ArrayList()
    var title: String = ""

    @Before
    fun createRepository() {
        localDataSource = FakeLocalDataSource(factCategory, allFacts)
        remoteDataSource = FakeRemoteDataSource(isConnected, response, title)
        factsDataRepository = FactsDataRepository(
            localDataSource, remoteDataSource, Dispatchers.Main
        )
    }

    @ExperimentalStdlibApi
    @ExperimentalCoroutinesApi
    @Test
    fun updateDataFromAPI_ConnectionError() = runBlockingTest {
        factsDataRepository.updateDataFromAPIServer()
    }


}