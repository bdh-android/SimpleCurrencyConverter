package com.example.simplecurrencyconverter.data.locale.database


import com.example.simplecurrencyconverter.data.DataSourceAdapter
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Constants.TAG
import com.example.simplecurrencyconverter.util.Result

class LocalDataSource(val dao: CurrencyDao) : DataSourceAdapter() {

    override suspend fun insertMultiRates(rates: List<CurrencyRates>) {
        try{
            dao.insertRates(rates)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getALLRates(base: String): Result<List<CurrencyRates>> {
        try {
            return Result.Success(dao.getALLRates(base))
        }catch (e:Exception){
            return  Result.Error(emptyList(),msg = "DatabaseError_${e.message}")
        }


    }

    override suspend fun getSingleRate(from: String, to: String): Result<CurrencyRates> {

        try {

            val cachedData = dao.getSingleRate(from, to)
            if (cachedData!=null)
            return Result.Success(cachedData )
            else return Result.Error(CurrencyRates(),msg ="")
        }catch (e:Exception){
            return  Result.Error(CurrencyRates() , msg = "DatabaseError_${e.message}")
        }
    }

    override suspend fun insertSingleRate(rate: CurrencyRates) {
        try{
            dao.insertRate(rate)
        }catch (e:Exception){e.printStackTrace()
        }
    }
}