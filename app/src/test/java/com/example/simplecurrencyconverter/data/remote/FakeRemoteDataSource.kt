package com.example.simplecurrencyconverter.data.remote

import com.example.simplecurrencyconverter.data.DataSourceAdapter
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Result

class FakeRemoteDataSource : DataSourceAdapter() {

    var withEror = false
    val freshData = listOf(
        CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14"),
        CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-14"),
        CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-14"),
        CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-14"),
        CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-14"),
        CurrencyRates("EURUSD","EUR","USD",1.23,"2022-9-14"),
        CurrencyRates("EURAFN","EUR","AFN",4.0,"2022-9-14"),
        CurrencyRates("EURALL","EUR","ALL",122.34,"2022-9-14"),
        CurrencyRates("EURAMD","EUR","AMD",44.0,"2022-9-14"),
        CurrencyRates("EURANG","EUR","ANG",13.0004,"2022-9-14"),
    )



    override suspend fun getALLRates(base: String): Result<List<CurrencyRates>> {
        val rates = mutableListOf<CurrencyRates>()
        if (!withEror){
            freshData.filter {
            it.id.startsWith(base)
        }.forEach {
            rates.add(it)
        }
            return Result.Success(rates)
        }else{
            return Result.Error(rates,"network error")
        }

    }

    override suspend fun getSingleRate(from: String, to: String): Result<CurrencyRates> {
        var rate: CurrencyRates = CurrencyRates()
        if (!withEror) {
            freshData.filter {
                it.id.equals(from + to)
            }.take(1).forEach {
                rate = it
            }
            return Result.Success(rate)
        }else{
            return Result.Error(rate,"network error")
        }
    }

    fun reset() {
        withEror = false
    }
}