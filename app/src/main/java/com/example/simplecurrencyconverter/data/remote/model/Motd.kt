package com.example.simplecurrencyconverter.data.remote.model

import com.google.gson.annotations.SerializedName


data class Motd (

  @SerializedName("msg" ) var msg : String ,
  @SerializedName("url" ) var url : String

)