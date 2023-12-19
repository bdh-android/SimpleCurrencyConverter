package com.example.simplecurrencyconverter.data.locale.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates

@Database(entities = [CurrencyRates::class], version = 1,exportSchema=false)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}