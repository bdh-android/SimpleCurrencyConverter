package com.example.simplecurrencyconverter.data.remote

import com.example.simplecurrencyconverter.data.DataSourceAdapter
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Result
import java.io.IOException

class RemoteDataSource (val api:  ApiInterface) : DataSourceAdapter(){

    override suspend fun getALLRates(base: String): Result<List<CurrencyRates>> {

        try {
            val response = api.getRates(base)
            if (response.isSuccessful){
                return Result.Success(response.body()?.toCurrencyRate()?: emptyList())
            }else{
                return Result.Error(emptyList(),msg="Network Error: ${response.message()} ${response.code()}")
            }
        }catch (ioException:IOException){
            return Result.Error(emptyList(),msg="Check Your Network Connection ")
        }catch (e:Exception){
            return Result.Error(emptyList(),msg="Something Went Wrong ")
        }




    }

    override suspend fun getSingleRate(from: String, to: String): Result<CurrencyRates> {


        try {
            val response =  api.convert(from,to)
            if (response.isSuccessful){
                return Result.Success(response.body()?.toCurrencyRate()?: CurrencyRates())
            }else{
                return Result.Error(CurrencyRates(),msg="Network Error: ${response.message()} ${response.code()}")
            }
        }catch (ioException:IOException){
            return Result.Error(CurrencyRates(),msg="Check Your Network Connection ${ioException.message}")
        }catch (e:Exception){
            return Result.Error(CurrencyRates(),msg="Something Went Wrong ${e.message}")
        }
    }
}