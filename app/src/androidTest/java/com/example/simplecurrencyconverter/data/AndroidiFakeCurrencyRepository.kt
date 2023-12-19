package com.example.simplecurrencyconverter.data

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.di.MainDispatcher
import com.example.simplecurrencyconverter.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class AndroidiFakeCurrencyRepository @Inject  constructor( @MainDispatcher val dispatcher:CoroutineDispatcher) : CurrencyRepository {

    var FromFlow  = MutableStateFlow("USD")
    var BaseFlow  = MutableStateFlow("USD")
    var ToFlow  = MutableStateFlow("EUR")

    var shouldSendEmptyData=false
    var shouldThrowError=false

    companion object{
        val MESSAGE_ERROR_WITH_DATA="error with data"
        val MESSAGE_ERROR_WITHOUT_DATA="error with out data"
    }
    override fun getAllApiRatesBasedOnBaseCurrency(base: String): Flow<Result<List<CurrencyRates>>> = flow{


        val rates =  data.filter {
            it.id.startsWith(base)
        }

        emit(Result.Loading(emptyList()))

        if (shouldSendEmptyData){
            emit(Result.Loading(emptyList()))

        }else {
            emit(Result.Loading(rates))

        }

        if (!shouldThrowError && !shouldSendEmptyData)
        emit(Result.Success(rates))
        else if (shouldThrowError && !shouldSendEmptyData) {
            emit(Result.Error(rates, MESSAGE_ERROR_WITH_DATA))
        }
        else {
            emit(Result.Error(emptyList(), MESSAGE_ERROR_WITHOUT_DATA))
        }



    }.flowOn(dispatcher)


    override fun getApiConversionFromCurrencyToAnother(
        from: String,
        to: String
    ): Flow<Result<CurrencyRates>> = flow {
        emit(Result.Loading(CurrencyRates()))
       val rate =  data.filter {
            it.fromCurrency == from && it.toCurrency == to
        }.first()

        if (shouldSendEmptyData){
            emit(Result.Loading(CurrencyRates()))

        }else {
            emit(Result.Loading(rate))

        }

        if (!shouldThrowError && !shouldSendEmptyData) {
            emit(Result.Success(rate))

        } else if (shouldThrowError && !shouldSendEmptyData) {
            emit(Result.Error(rate, MESSAGE_ERROR_WITH_DATA))

        }
        else {
            emit(Result.Error(CurrencyRates(), MESSAGE_ERROR_WITHOUT_DATA))

        }

    }.flowOn(dispatcher)



    override fun getBaseSymbol(): Flow<String>  = BaseFlow

    override suspend fun setBaseSymbol(base: String) {
        BaseFlow.value=base
    }

    override fun getFromSymbol(): Flow<String> = FromFlow
    override suspend fun setFromSymbol(from: String) {
           FromFlow.value = from
    }

    override fun getToSymbol(): Flow<String> = ToFlow

    override suspend fun setToSymbol(to: String) {
        ToFlow.value = to
    }

    val data :List<CurrencyRates> = listOf(
        CurrencyRates("USDUSD","USD","USD",1.0,"2022-9-14"),
        CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14"),
        CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-14"),
        CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-14"),
        CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-14"),
        CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-14"),

        CurrencyRates("EUREUR","EUR","EUR",1.0,"2022-9-14"),
        CurrencyRates("EURUSD","EUR","USD",1.0004,"2022-9-14"),
        CurrencyRates("EURAFN","EUR","AFN",4.0,"2022-9-14"),
        CurrencyRates("EURALL","EUR","ALL",122.34,"2022-9-14"),
        CurrencyRates("EURAMD","EUR","AMD",44.0,"2022-9-14"),
        CurrencyRates("EURANG","EUR","ANG",13.0004,"2022-9-14"),
    )
}