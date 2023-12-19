package com.example.simplecurrencyconverter.features.rates

import app.cash.turbine.test
import com.example.simplecurrencyconverter.data.FakeCurrencyRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
@ExperimentalCoroutinesApi
class RatesViewModelTest {

    private lateinit var fakeCurrencyRepository:FakeCurrencyRepository
    private lateinit var ratesViewModel: RatesViewModel
    val job= Job()
    val testDispatcher=StandardTestDispatcher()
    val testScope= TestScope(testDispatcher + job )
    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        fakeCurrencyRepository = FakeCurrencyRepository(testDispatcher)
        ratesViewModel= RatesViewModel(fakeCurrencyRepository)
    }
    @After
    fun tearDown(){
            Dispatchers.resetMain()
    }

    @Test
    fun `update uistate with success response`()= testScope.runTest {
       val data =fakeCurrencyRepository.data
       val job = launch {
           ratesViewModel.allRates.test {

               val firstEmission = awaitItem()
               val secondEmission = awaitItem()
               val thirdEmission = awaitItem()
               val forthEmission = awaitItem()
                assertNull("intial state is null",firstEmission)
               assertThat("intial loading state",secondEmission, instanceOf(RatesUIState.LoadingState::class.java))
               assertThat(" intial loading isloading : true",secondEmission?.isLoading, equalTo(true))
               assertThat(" loading state ",thirdEmission, instanceOf(RatesUIState.LoadingState::class.java))
               assertThat("  loading isloading : true",thirdEmission?.isLoading, equalTo(true))
               assertThat(" success state",forthEmission, instanceOf(RatesUIState.SuccessState::class.java))
               assertThat(" success isloading : false",forthEmission?.isLoading, equalTo(false))
               assertThat(" success state data",forthEmission?.data, equalTo(data))
               cancelAndConsumeRemainingEvents()
           }
       }



        job.cancel()
    }




    @Test
    fun `update uistate with error response`()= testScope.runTest {

        fakeCurrencyRepository.shouldThrowError = true

        val job1 = launch {
            ratesViewModel.allRates.test {
                val previousErrorState  = awaitItem()
                val emptyLoadingState = awaitItem()
                val  withDataLoadingState = awaitItem()
                val  errorState = awaitItem()

                assertNull(previousErrorState)
                assertThat(emptyLoadingState, instanceOf(RatesUIState.LoadingState::class.java))
                assertThat(withDataLoadingState, instanceOf(RatesUIState.LoadingState::class.java))
                assertThat(errorState, instanceOf(RatesUIState.ErrorState::class.java))

            }
        }



        job1.join()
        job1.cancel()
    }



}