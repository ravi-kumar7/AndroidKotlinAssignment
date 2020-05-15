package com.example.androidkotlinassignment

import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Test
    fun testTitle() {
        ActivityScenario.launch(MainActivity::class.java)
        onData(allOf(instanceOf(TextView::class.java), Matchers.equalTo("About Canada")))
        onData(
            allOf(
                instanceOf(TextView::class.java),
                Matchers.equalTo("Transportation")
            )
        ) //.perform(ViewActions.scrollTo())
        onData(allOf(instanceOf(TextView::class.java), Matchers.equalTo("Eh")))
        onView(withId(R.id.swipe_refresh_view)).perform(ViewActions.swipeDown())
        onView(withText(R.string.syncingData)).inRoot(ToastMatcher()).check(matches(isDisplayed()));
    }
}