package com.example.simplecurrencyconverter.data

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Result
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    fun getAllApiRatesBasedOnBaseCurrency(base: String): Flow<Result<List<CurrencyRates>>>

    //suspend fun getAllCashedRatesBasedOnBaseCurrency(base:String): Flow<Result<List<CurrencyRates>>>
    fun getApiConversionFromCurrencyToAnother(from: String, to: String): Flow<Result<CurrencyRates>>

    fun getBaseSymbol(): Flow<String>

    suspend fun setBaseSymbol(base: String)

    fun getFromSymbol(): Flow<String>

    suspend fun setFromSymbol(from: String)

    fun getToSymbol(): Flow<String>

    suspend fun setToSymbol(to: String)
}