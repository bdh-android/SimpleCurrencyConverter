package com.example.simplecurrencyconverter.data.locale.database

import com.example.simplecurrencyconverter.data.DataSourceAdapter
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Result

class FakeLocalDataSource : DataSourceAdapter() {

    var withEror = false
    var database = mutableMapOf<String,CurrencyRates>()

    override suspend fun insertMultiRates(rates: List<CurrencyRates>) {
        if(!rates.isEmpty()){
            rates.forEach {
                database.put(it.fromCurrency+it.toCurrency, it)
            }
        }

    }

    override suspend fun getALLRates(base: String): Result<List<CurrencyRates>> {
       val rates = mutableListOf<CurrencyRates>()
       if (!withEror){
           database.keys.filter {
           it.startsWith(base)
       }.forEach {

           database.get(it)?.let { it1 -> rates.add(it1) }
       }
           return Result.Success(rates)
       }else{
           return Result.Error(rates,"database error")
       }

    }

    override suspend fun getSingleRate(from: String, to: String): Result<CurrencyRates> {
       var rate:CurrencyRates= CurrencyRates()
        if (!withEror) {
            database.keys.filter {
                it.equals(from + to)
            }.take(1).forEach {
             rate = database.get(it)!!
            }
            return Result.Success(rate)
        }else{
            return Result.Error(rate,"database error")
        }
    }

    override suspend fun insertSingleRate(rate: CurrencyRates) {
        if (!rate.equals(null))
        database.put(rate.fromCurrency+rate.toCurrency , rate)
    }

    fun reset() {
        database.clear()
        withEror = false
    }
}