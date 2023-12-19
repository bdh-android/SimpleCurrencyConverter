package com.example.simplecurrencyconverter.data.locale.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates

@Dao
interface CurrencyDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertRates(rates:List<CurrencyRates>)

    @Query("SELECT * FROM currency_rates WHERE `from` Like :base")
    suspend fun getALLRates(base : String): List<CurrencyRates>

    @Query("SELECT * FROM currency_rates WHERE `from` Like :from And `to` like :to")
   suspend fun getSingleRate(from : String,to : String): CurrencyRates
    @Insert(onConflict = REPLACE)
    suspend fun insertRate(rate: CurrencyRates)

}