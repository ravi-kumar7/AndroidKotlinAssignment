package com.example.androidkotlinassignment.ui.fragments

import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.androidkotlinassignment.FakeAndroidFactsDataRepository
import com.example.androidkotlinassignment.R
import com.example.androidkotlinassignment.ServiceLocator
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.collections.ArrayList

@MediumTest
@RunWith(AndroidJUnit4::class)
class FactsFragmentTest {

    private lateinit var factsDataRepository: FakeAndroidFactsDataRepository


    @Before
    fun initRepository() {
        factsDataRepository = FakeAndroidFactsDataRepository()
        ServiceLocator.factsDataRepository = factsDataRepository
    }

    @After
    fun cleanUp() {
        ServiceLocator.resetRepository()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun factsTest_Loading() {
        launchFragmentInContainer<FactsFragment>(null, R.style.AppTheme)
        onView(withId(R.id.tv_loading_text)).check(matches(withText("loading…")))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun factsDataTest() {
        val testFactCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        val facts = ArrayList<Fact>()
        facts.add(Fact("1", "Test title 1", "Dummy description 1", "DUMMY 1", testFactCategory.id))
        facts.add(Fact("2", "Test title 3", "Dummy description 2", "DUMMY 2", testFactCategory.id))
        facts.add(Fact("3", "Test title 2", "Dummy description 3", "DUMMY 3", testFactCategory.id))
        factsDataRepository.setupData("Data synced with the server.", testFactCategory, facts, true)
        launchFragmentInContainer<FactsFragment>(null, R.style.AppTheme)
        onData(equalTo(facts[0]))
        onData(equalTo(facts[1]))
        onData(equalTo(facts[2]))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun factsDataTest_NoData() {
        val testFactCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        val facts = ArrayList<Fact>()
        factsDataRepository.setupData("Data synced with the server.", testFactCategory, facts, true)
        launchFragmentInContainer<FactsFragment>(null, R.style.AppTheme)
        onData(allOf(instanceOf(TextView::class.java), equalTo("No data.")))
    }

}