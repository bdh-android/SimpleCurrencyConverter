package com.example.simplecurrencyconverter.data

import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class FakeCurrencyRepository(val dispatcher:CoroutineDispatcher) : CurrencyRepository {

    var FromFlow  = MutableStateFlow("USD")
    var BaseFlow  = MutableStateFlow("USD")
    var ToFlow  = MutableStateFlow("EUR")
    val data :List<CurrencyRates> = listOf(
        CurrencyRates("USDEUR","USD","EUR",1.0004,"2022-9-14"),
        CurrencyRates("USDAFN","USD","AFN",4.0,"2022-9-14"),
        CurrencyRates("USDALL","USD","ALL",122.34,"2022-9-14"),
        CurrencyRates("USDAMD","USD","AMD",44.0,"2022-9-14"),
        CurrencyRates("USDANG","USD","ANG",13.0004,"2022-9-14"),
        )
    var shouldSendEmptyData=false
    var shouldThrowError=false

    companion object{
        val MESSAGE_ERROR_WITH_DATA="error with data"
        val MESSAGE_ERROR_WITHOUT_DATA="error with out data"
    }
    override fun getAllApiRatesBasedOnBaseCurrency(base: String): Flow<Result<List<CurrencyRates>>> = flow{

        emit(Result.Loading(emptyList()))

        if (shouldSendEmptyData){
            emit(Result.Loading(emptyList()))

        }else {
            emit(Result.Loading(data))

        }

        if (!shouldThrowError && !shouldSendEmptyData)
        emit(Result.Success(data))
        else if (shouldThrowError && !shouldSendEmptyData) {
            emit(Result.Error(data, MESSAGE_ERROR_WITH_DATA))
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

        if (shouldSendEmptyData){
            emit(Result.Loading(CurrencyRates()))

        }else {
            emit(Result.Loading(data[0]))

        }

        if (!shouldThrowError && !shouldSendEmptyData) {
            emit(Result.Success(data[0]))

        } else if (shouldThrowError && !shouldSendEmptyData) {
            emit(Result.Error(data[0], MESSAGE_ERROR_WITH_DATA))

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


}