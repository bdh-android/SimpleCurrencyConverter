package com.example.simplecurrencyconverter.data

import com.example.simplecurrencyconverter.data.locale.database.FakeLocalDataSource
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.data.locale.preferences.DataStorePreference
import com.example.simplecurrencyconverter.data.remote.FakeRemoteDataSource
import com.example.simplecurrencyconverter.util.Result
import kotlinx.coroutines.*

import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*


@ExperimentalCoroutinesApi
class CurrencyRepositoryImplTest {

    lateinit var  fakeLocalDataSource:FakeLocalDataSource
    lateinit var  fakeRemoteDataSource : FakeRemoteDataSource
    lateinit var currencyRepositoryImpl:CurrencyRepositoryImpl
    val preferences:DataStorePreference= mock(DataStorePreference::class.java)


    val job=Job()
    val dispacher=StandardTestDispatcher()
    val testScope= TestScope(dispacher + job )
    @Before
    fun setUp() {
        fakeLocalDataSource=FakeLocalDataSource()
        fakeRemoteDataSource=FakeRemoteDataSource()
        currencyRepositoryImpl=
            CurrencyRepositoryImpl(fakeRemoteDataSource,fakeLocalDataSource,preferences,dispacher)
    }

    @After
    fun tearDown() {
        fakeLocalDataSource.reset()
        fakeRemoteDataSource.reset()
    }

    @Test
    fun `currency rates based on basecurrency giving network on and no cached data _ return success result and loading without cached data`() = testScope.runTest{



         val expected:List<CurrencyRates>  = listOf(CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14"),
             CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-14"),
             CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-14"),
             CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-14"),
             CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-14"),

         )


       val flow = currencyRepositoryImpl.getAllApiRatesBasedOnBaseCurrency("USD")

        var result:List<Result<List<CurrencyRates>>> =  emptyList()
           launch {
             result = flow.take(3).toList()
               print(" ${result.get(2).data}")

               assertThat("initial loading result",result[0], instanceOf(Result.Loading::class.java))
               assertThat("loading result",result[1], instanceOf(Result.Loading::class.java))
               assertThat("success result",result[2], instanceOf(Result.Success::class.java))
               assertThat("initial loading result without data",result[0].data, equalTo(emptyList<CurrencyRates>()))
               assertThat("loading result without data",result[1].data, equalTo(emptyList<CurrencyRates>()))
               assertThat("  success result data  ",result[2].data, equalTo(expected))

           }


    }

    @Test
    fun `currency rates based on basecurrency giving network on and cached data _ return success result and loading with cached data`() = testScope.runTest{

        val expected:List<CurrencyRates>  = listOf(CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14"),
            CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-14"),
            CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-14"),
            CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-14"),
            CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-14"),

            )
        val cached =listOf(CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-12"),
            CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-12"),
            CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-12"),
            CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-12"),
            CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-12"),

            )

        fakeLocalDataSource.insertMultiRates(cached)
        val flow = currencyRepositoryImpl.getAllApiRatesBasedOnBaseCurrency("USD")

        var result:List<Result<List<CurrencyRates>>> =  emptyList()
        launch {
            result = flow.take(3).toList()
            print(" ${result.get(2).data}")

            assertThat("initial loading result without data",result[0].data, equalTo(emptyList<CurrencyRates>()))
            assertThat("loading result without data",result[1].data, equalTo(cached))
            assertThat("  success result data  ",result[2].data, equalTo(expected))


        }
    }
    @Test
    fun `currency rates based on basecurrency giving network error and cached data _ return both error result and loading with cached data`() = testScope.runTest{
        fakeRemoteDataSource.withEror =true

        val cached =listOf(CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-12"),
            CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-12"),
            CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-12"),
            CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-12"),
            CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-12"),

            )

        fakeLocalDataSource.insertMultiRates(cached)
        val flow = currencyRepositoryImpl.getAllApiRatesBasedOnBaseCurrency("USD")

        var result:List<Result<List<CurrencyRates>>> =  emptyList()
        launch {
            result = flow.take(3).toList()
            print(" ${result.get(2).data}")
            assertThat("success result",result[2], instanceOf(Result.Error::class.java))
            assertThat("initial loading result without data",result[0].data, equalTo(emptyList<CurrencyRates>()))
            assertThat("loading result without data",result[1].data, equalTo(cached))
            assertThat("  error result data  ",result[2].data, equalTo(cached))


        }
    }
    @Test
    fun `currency rates based on basecurrency giving network error and no cached data _ return both error result and loading without cached data`() = testScope.runTest{
        fakeRemoteDataSource.withEror =true

        val flow = currencyRepositoryImpl.getAllApiRatesBasedOnBaseCurrency("USD")

        var result:List<Result<List<CurrencyRates>>> =  emptyList()
        launch {
            result = flow.take(3).toList()
            print(" ${result.get(2).data}")
            assertThat("success result",result[2], instanceOf(Result.Error::class.java))
            assertThat("initial loading result without data",result[0].data, equalTo(emptyList<CurrencyRates>()))
            assertThat("loading result without data",result[1].data, equalTo(emptyList<CurrencyRates>()))
            assertThat("  error result data  ",result[2].data, equalTo(emptyList<CurrencyRates>()))


        }
    }
    @Test
    fun `currency rates based on basecurrency giving network on and database error _ return success result and loading without cached data`() =
        testScope.runTest{
        fakeLocalDataSource.withEror =true

        val expected:List<CurrencyRates>  = listOf(CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14"),
            CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-14"),
            CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-14"),
            CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-14"),
            CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-14"),

            )
        val cached:List<CurrencyRates>  = listOf(CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-12"),
            CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-12"),
            CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-12"),
            CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-12"),
            CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-12"),

            )
        fakeLocalDataSource.insertMultiRates(cached)

        println(fakeRemoteDataSource.withEror)
        val flow = currencyRepositoryImpl.getAllApiRatesBasedOnBaseCurrency("USD")

        var result:List<Result<List<CurrencyRates>>> =  emptyList()
        launch {
            result = flow.take(3).toList()
            print(" ${result.get(2).data}")

            assertThat("initial loading result",result[0], instanceOf(Result.Loading::class.java))
            assertThat("loading result",result[1], instanceOf(Result.Loading::class.java))
            assertThat("success result",result[2], instanceOf(Result.Success::class.java))
            assertThat("initial loading result without data",result[0].data, equalTo(emptyList<CurrencyRates>()))
            assertThat("loading result without data",result[1].data, equalTo(emptyList<CurrencyRates>()))
            assertThat(" success result data  ",result[2].data, equalTo(expected))


        }

    }



    @Test
    fun `Convert from currency to another with network on and data is cached in database return loading with cached data and success with fresh data`() = testScope.runTest{

        val expected= CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14")
        val cached=expected.copy(date="2022-9-12")
       fakeLocalDataSource.insertSingleRate(cached)
        val flow = currencyRepositoryImpl.getApiConversionFromCurrencyToAnother("USD","EUR")
        var destination:List<Result<CurrencyRates>> = emptyList()
       val job= launch {
            destination = flow.toList()

        }

        yield()
        println(destination)

        job.join()
        assertThat("loading result",destination[1],instanceOf(Result.Loading::class.java))
        assertEquals("loading result with cached data",cached, destination[1].data)
        assertThat("success result",destination[2],instanceOf(Result.Success::class.java))
        assertEquals("success result with fresh data",expected, destination[2].data)
        job.cancel()
    }
    @Test
    fun `Convert from currency to another with network on and data is not cached in database return loading without cached data and success with fresh data`() = testScope.runTest{

        val expected= CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14")
        val empty=CurrencyRates("","","",0.0,"")

        val flow = currencyRepositoryImpl.getApiConversionFromCurrencyToAnother("USD","EUR")
        var destination:List<Result<CurrencyRates>> = emptyList()
        val job= launch {
            destination = flow.toList()

        }

        yield()
        println(destination)

        job.join()
        assertThat("loading result",destination[1],instanceOf(Result.Loading::class.java))
        assertEquals("loading result with out cached data",empty, destination[1].data)
        assertThat("success result",destination[2],instanceOf(Result.Success::class.java))
        assertEquals("success result with fresh data",expected, destination[2].data)
        job.cancel()
    }
    @Test
    fun `Convert from currency to another giving that network error and data is cached in database return error with cached data`()=testScope.runTest {
        val expected= CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14")
        fakeLocalDataSource.insertSingleRate(expected)
        fakeRemoteDataSource.withEror=true
        val flow = currencyRepositoryImpl.getApiConversionFromCurrencyToAnother("USD","EUR")
        var destination:List<Result<CurrencyRates>> = emptyList()
        val job= launch {
            destination = flow.toList()

        }

        yield()
        println(destination)

        job.join()
        assertThat("loading result",destination[1],instanceOf(Result.Loading::class.java))
        assertEquals("loading with  data",expected, destination[1].data)
        assertThat("return value is error",destination[2],instanceOf(Result.Error::class.java))
        assertEquals("",expected, destination[2].data)
        job.cancel()
    }

    @Test
    fun `Convert from currency to another giving that network error and data is not cached in database return both loading and error results with empty data`()=testScope.runTest {
        val expected= CurrencyRates("","","",0.0,"")
        fakeRemoteDataSource.withEror=true
        val flow = currencyRepositoryImpl.getApiConversionFromCurrencyToAnother("USD","EUR")
        var destination:List<Result<CurrencyRates>> = emptyList()
        val job= launch {
            destination = flow.toList()

        }

        yield()
        println(destination)

        job.join()
        assertThat("loading result",destination[1],instanceOf(Result.Loading::class.java))
        assertEquals("loading with empty data",expected, destination[1].data)
        assertThat("error result",destination[2],instanceOf(Result.Error::class.java))
        assertEquals("error with empty data",expected, destination[2].data)
        job.cancel()
    }


}