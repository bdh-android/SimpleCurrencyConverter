package com.example.simplecurrencyconverter.data.remote.model

import com.google.gson.annotations.SerializedName


data class Query (

  @SerializedName("from"   ) var from   : String,
  @SerializedName("to"     ) var to     : String,
  @SerializedName("amount" ) var amount : Int

)