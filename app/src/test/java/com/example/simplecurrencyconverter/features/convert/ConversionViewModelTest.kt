package com.example.simplecurrencyconverter.features.convert

import app.cash.turbine.test
import com.example.simplecurrencyconverter.data.FakeCurrencyRepository
import com.example.simplecurrencyconverter.features.common.roundBy
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ConversionViewModelTest {

    private lateinit var fakeCurrencyRepository: FakeCurrencyRepository
    private lateinit var conversionViewModel: ConversionViewModel
    val job = Job()
    val testDispatcher = StandardTestDispatcher()
    val testScope = TestScope(testDispatcher + job)


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeCurrencyRepository = FakeCurrencyRepository(testDispatcher)
        conversionViewModel = ConversionViewModel(fakeCurrencyRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

    }

    @Test
    fun fetchCurrencyRatesBasedOnStoredCurrencySymbols_SuccessResultAndLoadingWithData()=testScope.runTest{

        val job = launch {

            conversionViewModel.uiState.test {
                val firstEmmission = awaitItem()//initial value

                 val secondEmission = awaitItem()//loading

                val thirdEmission = awaitItem()//loading with data

                 val forthEmission = awaitItem()// success

                assertThat("isloading should be true",secondEmission?.isLoading,equalTo(true))
                assertThat("isloading should be true again",thirdEmission?.isLoading,equalTo(true))
                assertThat("there price greater than zero",thirdEmission?.price,greaterThan(0.0))
                assertThat("is loading should be false when success",forthEmission?.isLoading,equalTo(false))
                assertThat("there price greater than zero",forthEmission?.price,greaterThan(0.0))



                cancelAndConsumeRemainingEvents()
            }
        }



        job.join()
        job.cancel()
    }
    @Test
    fun fetchCurrencyRatesBasedOnStoredCurrencySymbols_SuccessResultAndLoadingWithoutData()=testScope.runTest{

        fakeCurrencyRepository.shouldSendEmptyData = true
        val job = launch {

            conversionViewModel.uiState.test {
                val firstEmmission = awaitItem()//initial value

                val secondEmission = awaitItem()//loading

                val thirdEmission = awaitItem()//loading without data

                val forthEmission = awaitItem()// success

                assertThat("isloading should be true",secondEmission?.isLoading,equalTo(true))
                assertThat("isloading should be true again",thirdEmission?.isLoading,equalTo(true))
                assertThat("there price is zero",thirdEmission?.price,equalTo(0.0))
                assertThat("is loading should be false when success",forthEmission?.isLoading,equalTo(false))
                assertThat("there price greater than zero",forthEmission?.price,greaterThan(0.0))



                cancelAndConsumeRemainingEvents()
            }
        }



        job.join()
        job.cancel()
    }

    @Test
    fun fetchCurrencyRatesBasedOnStoredCurrencySymbols_FailureResultAndLoadingWithData()=testScope.runTest{

        fakeCurrencyRepository.shouldThrowError = true
        val job = launch {

            conversionViewModel.uiState.test {
                val firstEmmission = awaitItem()//initial value

                val secondEmission = awaitItem()//loading

                val thirdEmission = awaitItem()//loading with data

                val forthEmission = awaitItem()// Error

                assertThat("isloading should be true",secondEmission?.isLoading,equalTo(true))
                assertThat("isloading should be true again",thirdEmission?.isLoading,equalTo(true))
                assertThat("there price greater than zero",thirdEmission?.price, greaterThan(0.0))
                assertThat("is loading should be false when failure",forthEmission?.isLoading,equalTo(false))
                assertThat("there price greater than zero",forthEmission?.price,greaterThan(0.0))
                assertThat("there is error message",forthEmission?.errorMsg, equalTo(FakeCurrencyRepository.MESSAGE_ERROR_WITH_DATA))



                cancelAndConsumeRemainingEvents()
            }
        }



        job.join()
        job.cancel()
    }

    @Test
    fun fetchCurrencyRatesBasedOnStoredCurrencySymbols_FailureResultAndLoadingWithoutData()=testScope.runTest{

        fakeCurrencyRepository.shouldThrowError = true
        fakeCurrencyRepository.shouldSendEmptyData = true
        val job = launch {

            conversionViewModel.uiState.test {
                val firstEmmission = awaitItem()//initial value

                val secondEmission = awaitItem()//loading

                val thirdEmission = awaitItem()//loading with data

                val forthEmission = awaitItem()// Error


                assertThat("is loading should be false when failure",forthEmission?.isLoading,equalTo(false))
                assertThat("there price is zero",forthEmission?.price, equalTo(0.0))
                assertThat("there is error message",forthEmission?.errorMsg, equalTo(FakeCurrencyRepository.MESSAGE_ERROR_WITHOUT_DATA))



                cancelAndConsumeRemainingEvents()
            }
        }



        job.join()
        job.cancel()
    }
    @Test
    fun fetchCurrencyRatesAfterChangeFromSymbol_ChangeFromSymbolFeildtoNewerOne() = testScope.runTest {

        val job1 = launch {
            conversionViewModel.uiState.test {
                val firstEmmission = awaitItem()//initial value

                val secondEmission = awaitItem()

                val thirdEmission = awaitItem()

                val forthEmission = awaitItem()

                println(firstEmmission)

                println(secondEmission)

                println(thirdEmission)

                println(forthEmission)


                assertThat("from symbol should equal SYP",secondEmission?.fromSymbol, equalTo("SYP"))


                cancelAndConsumeRemainingEvents()
            }
        }


        conversionViewModel.Event(ConvertUiEvents.fromSpinnerChanged("SYP"))
        advanceUntilIdle()


        job1.join()
        job1.cancel()


    }
    @Test
    fun afterChangeFromText_UpdateToText() = testScope.runTest {
        var price : Double?=0.0
        val job1 = launch {
            conversionViewModel.uiState.test {
                val firstEmmission = awaitItem()//initial value

                val secondEmission = awaitItem()

                val thirdEmission = awaitItem()

                val forthEmission = awaitItem()

                price=forthEmission?.price

                cancelAndConsumeRemainingEvents()
            }
        }



         advanceUntilIdle()
         conversionViewModel.Event(ConvertUiEvents.fromEditTextChanged("2"))
         assertThat(conversionViewModel.uiState.value?.toText, equalTo(price?.times(2).toString()))

        job1.join()
        job1.cancel()



    }

    @Test fun roundNumber(){

        assertThat(4.2134.roundBy(),`is`(4.22.toString()) )
        assertThat(4.215.roundBy(),`is`(4.22.toString()) )
        assertThat(4.5134.roundBy(),`is`(4.52.toString()) )

    }
}