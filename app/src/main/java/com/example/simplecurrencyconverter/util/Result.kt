package com.example.simplecurrencyconverter.util


sealed class Result<T> (val data:T,val msg : String?){

    class Loading<T>(data: T):Result<T>(data,null)
    class Success<T>(data: T) : Result<T>(data,null)
    class Error<T>(data: T,msg: String) : Result<T>(data ,msg)


}