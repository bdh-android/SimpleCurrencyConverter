package com.example.simplecurrencyconverter.features.convert

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simplecurrencyconverter.R
import com.example.simplecurrencyconverter.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest

@RunWith(AndroidJUnit4::class)
class ConvertFragmentTest{


    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @Before
    fun setUp(){
        hiltRule.inject()

    }
    @Test
    fun launchFragment(){
        launchFragmentInHiltContainer<ConvertFragment> {  }
        onView(withId(R.id.spinner_from)).perform(click())
        onData(allOf(instanceOf(String::class.java),`is`("USD"))).perform(click())
        onView(withId(R.id.spinner_from)).check(matches(withSpinnerText(containsString("USD"))))

		onView(withId(R.id.spinner_to)).perform(click())
        onData(allOf(instanceOf(String::class.java),`is`("EUR"))).perform(click())
        onView(withId(R.id.spinner_to)).check(matches(withSpinnerText(containsString("EUR"))))
		


    }
}