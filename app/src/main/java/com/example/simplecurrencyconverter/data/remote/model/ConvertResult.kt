package com.example.simplecurrencyconverter.data.remote.model

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.convertTimestampToString
import com.google.gson.annotations.SerializedName


data class ConvertResult (

 // @SerializedName("motd"       ) var motd       : Motd ,
  @SerializedName("success"    ) var success    : Boolean ,
  @SerializedName("query"      ) var query      : Query   ,
  @SerializedName("info"       ) var info       : Info ,
 // @SerializedName("historical" ) var historical : Boolean ,
  //@SerializedName("date"       ) var date       : String ,
  @SerializedName("result"     ) var result     : Double

){
  fun toCurrencyRate(): CurrencyRates {
    return CurrencyRates(query.from+query.to,
      query.from,
      query.to,result,
      convertTimestampToString(info.timeStamp*1000))
  }
}