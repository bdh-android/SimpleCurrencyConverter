package com.example.simplecurrencyconverter.data.locale.database

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceTest {

    val dao:CurrencyDao = mock(CurrencyDao::class.java)
   lateinit var localDataSource: LocalDataSource
    @Before
    fun setUp(){
        localDataSource= LocalDataSource(dao)
    }
    @Test
    fun `get multi rows from database _return success result`()= runTest {
        val list :List<CurrencyRates> = listOf(CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-12"),
        CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-12"),
        )
        whenever(dao.getALLRates("USD")).thenReturn(list)
        val result = localDataSource.getALLRates("USD")
        assertThat("success result",result,instanceOf(Result.Success::class.java))
        assertThat("same list ",result.data, equalTo(list))
    }

    @Test
    fun `throw error when reading from database _ return error result with empty list`()= runTest {

        whenever(dao.getALLRates("USD")).thenThrow(RuntimeException("error reading from database"))
        val result = localDataSource.getALLRates("USD")
        assertThat("error result",result,instanceOf(Result.Error::class.java))
        assertThat("same list ",result.data, equalTo(emptyList()))
        assertThat("error message ",result.msg, equalTo("DatabaseError_error reading from database"))
    }

    @Test
    fun `get single row from database _return success result`()= runTest {
        val value =CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-12")
        whenever(dao.getSingleRate("USD","EUR")).thenReturn(value)
        val result = localDataSource.getSingleRate("USD","EUR")
        assertThat("success result",result,instanceOf(Result.Success::class.java))
        assertThat("same data ",result.data, equalTo(value))
    }

    @Test
    fun `throw error when reading single row from database _ return error result with empty response`()= runTest {

        whenever(dao.getSingleRate("USD","EUR")).thenThrow(RuntimeException("error reading from database"))
        val result = localDataSource.getSingleRate("USD","EUR")
        assertThat("error result",result,instanceOf(Result.Error::class.java))
        assertThat("same list ",result.data, equalTo(CurrencyRates("","","",0.0,"")))
        assertThat("error message ",result.msg, equalTo("DatabaseError_error reading from database"))
    }

    @Test
    fun `insert multi values into database _ call correct function from dao `()= runTest {
        val list :List<CurrencyRates> = emptyList()
       localDataSource.insertMultiRates(list)
        verify(dao, times(1)).insertRates(list)
    }

    @Test
    fun `insert single value into database _ call correct function from dao `()= runTest {
        val value =CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-12")
        localDataSource.insertSingleRate(value)
        verify(dao, times(1)).insertRate(value)
    }
}