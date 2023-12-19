package com.example.simplecurrencyconverter.data


import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.data.locale.preferences.PreferencesInterface
import com.example.simplecurrencyconverter.di.DataStoreQualifier
import com.example.simplecurrencyconverter.di.IODispatcher
import com.example.simplecurrencyconverter.di.LocalDataSourceQualifier
import com.example.simplecurrencyconverter.di.RemoteDataSourceQualifier
import com.example.simplecurrencyconverter.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    @RemoteDataSourceQualifier private val remoteDataSource: DataSource,
    @LocalDataSourceQualifier private val localDataSource: DataSource,
    @DataStoreQualifier private val preferences: PreferencesInterface,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : CurrencyRepository {


    override fun getAllApiRatesBasedOnBaseCurrency(base: String): Flow<Result<List<CurrencyRates>>> =
        flow {
            emit(Result.Loading(emptyList()))

            var cashedData = emptyList<CurrencyRates>()


            cashedData = localDataSource.getALLRates(base).data
            emit(Result.Loading(cashedData))

            val freshData = remoteDataSource.getALLRates(base)
            when (freshData) {
                is Result.Success -> {
                    emit(Result.Success(freshData.data))
                    localDataSource.insertMultiRates(freshData.data)
                }
                is Result.Error -> {
                    emit(Result.Error(cashedData, freshData.msg.toString()))
                }
                else -> {
                    emit(Result.Error(cashedData, "ERROR.."))
                }
            }

        }.flowOn(dispatcher)


    override fun getApiConversionFromCurrencyToAnother(
        from: String,
        to: String
    ): Flow<Result<CurrencyRates>> = flow {

        emit(Result.Loading(CurrencyRates()))


        var cashedData = CurrencyRates()

        cashedData = localDataSource.getSingleRate(from, to).data
        emit(Result.Loading(cashedData))


        val freshData = remoteDataSource.getSingleRate(from, to)
        when (freshData) {
            is Result.Success -> {
                emit(Result.Success(freshData.data))
                localDataSource.insertSingleRate(freshData.data)
            }
            is Result.Error -> {
                emit(Result.Error(cashedData, freshData.msg.toString()))
            }
            else -> {
                emit(Result.Error(cashedData, "ERROR.."))
            }
        }


    }.flowOn(dispatcher)


    override fun getBaseSymbol(): Flow<String> {
        return preferences.getBaseSymbol()
    }

    override suspend fun setBaseSymbol(base: String) {
        preferences.saveBaseSymbol(base)
    }

    override fun getFromSymbol(): Flow<String> {
        return preferences.getFromSymbol()
    }

    override suspend fun setFromSymbol(from: String) {
        preferences.saveFromSymbol(from)
    }

    override fun getToSymbol(): Flow<String> {
        return preferences.getToSymbol()
    }

    override suspend fun setToSymbol(to: String) {
        preferences.saveToSymbol(to)
    }
}