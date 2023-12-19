package com.example.simplecurrencyconverter.features.convert

sealed class ConvertUiEvents {
    class fromSpinnerChanged(val fromSymbol:String):ConvertUiEvents()
    class toSpinnerChanged(val toSymbol:String):ConvertUiEvents()
    class fromEditTextChanged(val fromText:String):ConvertUiEvents()
    class toEditTextChanged(val toText:String):ConvertUiEvents()
}