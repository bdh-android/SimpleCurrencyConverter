package com.example.simplecurrencyconverter.data.locale.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "currency_rates")
 data class CurrencyRates(
    @PrimaryKey
    var id : String,
    @ColumnInfo(name = "from")
    var fromCurrency:String,
@ColumnInfo(name = "to")
var toCurrency:String,
@ColumnInfo(name = "price")
var price:Double,
@ColumnInfo(name = "date")
var date:String)
{
     constructor():this(
         "","","",0.0,""
     )

 }