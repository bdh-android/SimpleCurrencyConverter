package com.example.simplecurrencyconverter.data.remote

import com.example.simplecurrencyconverter.data.remote.model.ConvertResult
import com.example.simplecurrencyconverter.data.remote.model.ExchangeRatesResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("v1/latest")
    suspend fun getRates(@Query("base") base:String,
    ):Response<ExchangeRatesResult>
   @GET("v1/convert")
   suspend fun convert(@Query("from")from:String,
                       @Query("to")to:String,
                       @Query("amount")amount:String="1",

   ):Response<ConvertResult>
}// @Query("api_key")apiKey:String="ab8dd719e62d620f3d119f4e3a085be9"