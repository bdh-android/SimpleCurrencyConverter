package com.example.simplecurrencyconverter.data

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Result

open class DataSourceAdapter : DataSource {
    override suspend fun insertMultiRates(rates: List<CurrencyRates>) {
        TODO("Not yet implemented")
    }

    override suspend fun getALLRates(base: String): Result<List<CurrencyRates>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleRate(from: String, to: String): Result<CurrencyRates> {
        TODO("Not yet implemented")
    }


    override suspend fun insertSingleRate(rate: CurrencyRates) {
        TODO("Not yet implemented")
    }

}