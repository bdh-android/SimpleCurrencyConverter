package com.example.simplecurrencyconverter.features.rates

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates

sealed class RatesUIState(val data:List<CurrencyRates> = emptyList(),
                          val errorMsg:String? =null, val isLoading: Boolean =false) {
    class LoadingState(data:List<CurrencyRates>, isLoading: Boolean) : RatesUIState(data=data,isLoading=isLoading)
    class SuccessState(data:List<CurrencyRates>, isLoading: Boolean) : RatesUIState(data=data,isLoading=isLoading)
    class ErrorState(data:List<CurrencyRates>, errorMsg:String?, isLoading: Boolean):RatesUIState(data,errorMsg,isLoading)

}