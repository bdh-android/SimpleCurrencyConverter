package com.example.simplecurrencyconverter.features.rates


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecurrencyconverter.R
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simplecurrencyconverter.data.AndroidiFakeCurrencyRepository
import com.example.simplecurrencyconverter.data.CurrencyRepository
import com.example.simplecurrencyconverter.features.rates.RecyclerViewItemsCount.Companion.withItemCount
import com.example.simplecurrencyconverter.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RatesFragmentTest{

    @get:Rule
    val hiltRule =HiltAndroidRule(this)
    @Inject
   lateinit var reposetory : CurrencyRepository

    @Before
    fun setUp(){
        hiltRule.inject()

    }
    @Test
    fun launchFragment(){

        (reposetory as AndroidiFakeCurrencyRepository).shouldThrowError=true
        launchFragmentInHiltContainer<RatesFragment>(){

        }


        onView(withId(R.id.spinner_base)).perform(click())
        onData(allOf(instanceOf(String::class.java),`is`("EUR"))).perform(click())
        onView(withId(R.id.spinner_base)).check(matches(withSpinnerText(containsString("EUR"))))

        onView(withId(R.id.currencies_list)).check(withItemCount(6))

        onView(withId(R.id.currencies_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(hasDescendant(withText("EUR"))))

        onView(withText("error with data")).check(matches(isDisplayed()))
    }


}

class RecyclerViewItemsCount(var  matcher :Matcher<Int>) : ViewAssertion{

    companion object{
        fun withItemCount(expectedCount :Int):RecyclerViewItemsCount{
            return withItemCount(`is`(expectedCount))
        }
       fun withItemCount(matcher :Matcher<Int>):RecyclerViewItemsCount{
            return RecyclerViewItemsCount(matcher)
       }
    }
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
       if (noViewFoundException != null){
           throw noViewFoundException
       }
        val  recyclerView = view as RecyclerView
        assertThat(recyclerView.adapter?.itemCount,matcher)
    }

}