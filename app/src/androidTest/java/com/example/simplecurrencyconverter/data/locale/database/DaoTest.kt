package com.example.simplecurrencyconverter.data.locale.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DaoTest {
   @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var dao: CurrencyDao
    @Inject
    @Named("database_test")
     lateinit var database: CurrencyDatabase

    @Before
    fun setUp() {
        hiltAndroidRule.inject()

        dao = database.currencyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertMultiRowsInDatabase() = runTest {
        val expectedList = listOf(
            CurrencyRates("USDEUR", "USD", "EUR", 0.99, "2022-9-12"),
            CurrencyRates("USDAFN", "USD", "AFN", 4.0, "2022-9-14"),
            CurrencyRates("USDALL", "USD", "ALL", 122.34, "2022-9-14"),
            CurrencyRates("USDAMD", "USD", "AMD", 44.0, "2022-9-14"),
            CurrencyRates("USDANG", "USD", "ANG", 13.0004, "2022-9-14")
        )

        dao.insertRates(expectedList)

        val actualList: List<CurrencyRates> = dao.getALLRates("USD")
         println(actualList)
        assertEquals("", expectedList, actualList)
    }

    @Test
    fun insertSingleRowInDatabase() = runTest {
        val expected = CurrencyRates("USDEUR", "USD", "EUR", 0.99, "2022-9-12")

        dao.insertRate(expected)

        val actual: CurrencyRates = dao.getSingleRate("USD", "EUR")

        assertEquals("", expected, actual)
    }

    @Test
    fun updateSingleRowInDatabase() = runTest {
        val existedRow = CurrencyRates("USDEUR", "USD", "EUR", 0.99, "2022-9-12")

        dao.insertRate(existedRow)
        val expected = existedRow.copy(price=1.0)
        dao.insertRate(expected)

        val actual = dao.getALLRates("USD")
        assertEquals("Should be one row in database",actual.size,1)
        assertEquals("", expected, actual[0])
    }
    @Test
    fun getNullWhenDataIsnotThere()= runTest{
        val actual = dao.getALLRates("USD")

        assertEquals("should be empty list ", emptyList<CurrencyRates>(),actual)
    }
}
