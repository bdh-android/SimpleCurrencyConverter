package com.example.simplecurrencyconverter.data

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Result


interface DataSource {

    suspend fun insertMultiRates(rates: List<CurrencyRates>)

    suspend fun getALLRates(base : String): Result<List<CurrencyRates>>

    suspend fun getSingleRate(from : String,to : String): Result<CurrencyRates>

    suspend fun insertSingleRate(rate: CurrencyRates)

}