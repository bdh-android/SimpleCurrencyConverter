package com.example.simplecurrencyconverter.data.remote

import com.example.simplecurrencyconverter.TestUtils
import com.example.simplecurrencyconverter.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers.instanceOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDataSourceTest {

val body = TestUtils.fileReader("latest_base_usd.json")?:""
    lateinit var server: MockWebServer
    lateinit var baseUrl: HttpUrl
    lateinit var dataSource :RemoteDataSource

    val dispatcher = object : Dispatcher(){
        override fun dispatch(request: RecordedRequest): MockResponse {
           return when(request.path){
               "/v1/latest?base=USD" -> {
                   MockResponse().setBody(body)
                }
                "/v1/convert?from=USD&to=EUR&amount=1" -> {
                   MockResponse().setResponseCode(200).setBody(
                       TestUtils.fileReader("convertUSDtoEUR.json")?:"")
               }
               "/v1/convert?from=&to=&amount=1" -> {
                   MockResponse().setResponseCode(200).setBody(
                       TestUtils.fileReader("convert_missing_params.json")?:"")
               }
               "/v1/latest?base=error" -> {
                   MockResponse().setResponseCode(501)
               }
               else -> {
                   MockResponse().setResponseCode(404)
               }
            }
        }

    }



    @Before
    fun setUp() {

      server = MockWebServer()
        server.dispatcher = dispatcher

        server.start()
      baseUrl = server.url("")
      dataSource  = RemoteDataSource(provideApiInterface())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `get currency rates based on base currency _ return success result`() = runTest {
     val result = dataSource.getALLRates("USD")
       // print(result)
       // print(result.data)
       // print(result.msg)

        assertThat("success result",result,instanceOf(Result.Success::class.java))
        val request = server.takeRequest()
        print(request.requestLine)
        assertEquals("GET /v1/latest?base=USD HTTP/1.1", request.requestLine)

    }
    @Test
    fun `convert from usd into eur currency _ return success result`() = runTest {
        val result = dataSource.getSingleRate("USD","EUR")
        print(result)

        assertThat("success result",result,instanceOf(Result.Success::class.java))
        val request = server.takeRequest()
        assertEquals("GET /v1/convert?from=USD&to=EUR&amount=1 HTTP/1.1", request.requestLine)

    }
    @Test
    fun `convert with empty parameters _ return success result `() = runTest {
        val result = dataSource.getSingleRate("","")
        println(result)
        println(result.data)


        assertThat("success result",result,instanceOf(Result.Success::class.java))
        val request = server.takeRequest()
        println(request.path)
        println(request.requestLine)
        println(request.body)
        assertEquals("GET /v1/convert?from=&to=&amount=1 HTTP/1.1", request.requestLine)

    }

    @Test
    fun `server error 501 response _ return error result `() = runTest {
        val result = dataSource.getALLRates("error")
         println(result.msg)
        assertThat("error result",result,instanceOf(Result.Error::class.java))
    }
//ApiInterface
    fun provideApiInterface(): ApiInterface {
        return retrofit2.Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}