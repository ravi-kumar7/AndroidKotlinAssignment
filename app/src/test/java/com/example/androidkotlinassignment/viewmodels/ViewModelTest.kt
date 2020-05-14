package com.example.androidkotlinassignment.viewmodels

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidkotlinassignment.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(maxSdk = Build.VERSION_CODES.P)
class ViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun setStatusMsg() {
        val testValue = "This is a test status"
        val viewModel = MainViewModel(ApplicationProvider.getApplicationContext())
        viewModel.setStatusMsg(testValue)
        val output = viewModel.getStatusMsg().getOrAwaitValue()
        assertEquals(testValue, output)
    }
}