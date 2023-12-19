package com.example.simplecurrencyconverter.data.remote.model

import com.google.gson.annotations.SerializedName


data class Info (
  @SerializedName("quote") var rate : Double,
  @SerializedName("timestamp") var timeStamp : Long)