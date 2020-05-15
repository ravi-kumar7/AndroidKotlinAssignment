package com.example.androidkotlinassignment.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.models.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class FactsDataRepositoryTest {
    private lateinit var factsDataRepository: FactsDataRepository
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var remoteDataSource: FakeRemoteDataSource

    private val factCategory: MutableLiveData<FactCategory> = MutableLiveData()
    private val allFacts: MutableLiveData<List<Fact>> = MutableLiveData()
    var isConnected = false
    var response: ArrayList<Model.Row> = ArrayList()
    var title: String = ""

    @ExperimentalCoroutinesApi
    val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        Dispatchers.setMain(testDispatcher)
        localDataSource = FakeLocalDataSource(factCategory, allFacts)
        remoteDataSource = FakeRemoteDataSource(isConnected, response, title)
        factsDataRepository = FactsDataRepository(
            localDataSource, remoteDataSource, Dispatchers.Main
        )
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalStdlibApi
    @ExperimentalCoroutinesApi
    @Test
    fun updateDataFromAPI_ConnectionError() = runBlocking {
        val expected = Result.Error(Exception("Not able to connect")).toString()
        factsDataRepository.updateDataFromAPIServer()
        assertEquals(expected, factsDataRepository.status.value)
    }

    @ExperimentalStdlibApi
    @ExperimentalCoroutinesApi
    @Test
    fun updateDataFromAPI_Success() = runBlockingTest {
        val expected = "Data Synced with the Server"
        remoteDataSource.isConnected = true
        val rows = ArrayList<Model.Row>()
        rows.add(Model.Row("Test title 1", "Dummy description 1", "DUMMY 1"))
        rows.add(Model.Row("Test title 2", "Dummy description 2", "DUMMY 1"))
        rows.add(Model.Row("Test title 3", "Dummy description 3", "DUMMY 1"))
        val testTitle = "testTitle"
        remoteDataSource.title = testTitle
        remoteDataSource.response = rows
        factsDataRepository.updateDataFromAPIServer()
        assertEquals(expected, factsDataRepository.status.value)
    }


    @Test
    fun getFactCategoryTest() {
        val testFactCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        localDataSource.factCategory.postValue(testFactCategory)
        val result = factsDataRepository.factCategory
        assertEquals(testFactCategory, result.value)
    }

    @Test
    fun getFactsTest() {
        val testFactCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        val facts = ArrayList<Fact>()
        facts.add(Fact("1", "Test title 1", "Dummy description 1", "DUMMY 1", testFactCategory.id))
        facts.add(Fact("2", "Test title 3", "Dummy description 2", "DUMMY 2", testFactCategory.id))
        facts.add(Fact("3", "Test title 2", "Dummy description 3", "DUMMY 3", testFactCategory.id))
        localDataSource.allFacts.postValue(facts)
        val result = factsDataRepository.allFacts.value
        for (i in result!!.indices) {
            assertEquals(facts[i].title, result[i].title)
            assertEquals(facts[i].description, result[i].description)
            assertEquals(facts[i].imageHref, result[i].imageHref)
        }
    }

    @Test
    fun apiSyncStatusText() {
        val status = "Data Synced with server"
        factsDataRepository.setStatus(status)
        assertEquals(status, factsDataRepository.status.value)
    }


}