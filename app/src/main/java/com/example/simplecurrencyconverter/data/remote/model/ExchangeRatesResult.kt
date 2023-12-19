package com.example.simplecurrencyconverter.data.remote.model

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.convertTimestampToString
import com.google.gson.annotations.SerializedName


data class ExchangeRatesResult (

  //@SerializedName("motd"    ) var motd    : Motd ,
  @SerializedName("success" ) var success : Boolean ,
  @SerializedName("base"    ) var base    : String ,
 // @SerializedName("date"    ) var date    : String ,
  @SerializedName("timestamp"    ) var date    : Long ,
  @SerializedName("rates"   ) var rates   : Rates

){

  fun toCurrencyRate(): List<CurrencyRates> {
    val l = ArrayList<CurrencyRates>()
    for ( (k,v) in rates.toMap().entries){
      if(k==base){
        continue
      }
        l.add(CurrencyRates(base+k,base,k,v?:0.0,convertTimestampToString(date*1000)))
    }

   return l
  }


}