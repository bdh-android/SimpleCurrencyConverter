package com.example.simplecurrencyconverter.features.convert

data class ConvertUiState(val fromSymbol:String ,
                          val toSymbol:String ,
                          val price : Double ,
                          val date :String,
                          val fromText:String,
                          val toText:String,
                          val errorMsg:String?=null,
                          val isLoading:Boolean
                          )