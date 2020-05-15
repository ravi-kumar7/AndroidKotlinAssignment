package com.example.androidkotlinassignment.viewmodels

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidkotlinassignment.getOrAwaitValue
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.source.FakeFactsDataRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
@Config(maxSdk = Build.VERSION_CODES.P)
class ViewModelTest {

    private lateinit var factsDataRepository: FakeFactsDataRepository
    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        factsDataRepository = FakeFactsDataRepository()
        viewModel = MainViewModel(factsDataRepository)
    }


    @Test
    fun setStatusMsgTest() {
        val testValue = "This is a test status"
        viewModel.setStatusMsg(testValue)
        val output = viewModel.getStatusMsg().getOrAwaitValue()
        assertEquals(testValue, output)
    }

    @Test
    fun isDataSyncedTest() {
        factsDataRepository.isDataSynced.postValue(true)
        val output = viewModel.isDataSynced().getOrAwaitValue()
        assertEquals(true, output)
    }

    @Test
    fun getFactCategoryTest() {
        val testFactCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        factsDataRepository.factCategory.value = testFactCategory
        val result = viewModel.getFactCategory().getOrAwaitValue()
        assertEquals(testFactCategory, result)
    }

    @Test
    fun getFactsTest() {
        val testFactCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        val facts = ArrayList<Fact>()
        facts.add(Fact("1", "Test title 1", "Dummy description 1", "DUMMY 1", testFactCategory.id))
        facts.add(Fact("2", "Test title 3", "Dummy description 2", "DUMMY 2", testFactCategory.id))
        facts.add(Fact("3", "Test title 2", "Dummy description 3", "DUMMY 3", testFactCategory.id))
        factsDataRepository.allFacts.value = facts
        val result = factsDataRepository.allFacts.getOrAwaitValue()
        for (i in result!!.indices) {
            assertEquals(facts[i].title, result[i].title)
            assertEquals(facts[i].description, result[i].description)
            assertEquals(facts[i].imageHref, result[i].imageHref)
        }
    }

}