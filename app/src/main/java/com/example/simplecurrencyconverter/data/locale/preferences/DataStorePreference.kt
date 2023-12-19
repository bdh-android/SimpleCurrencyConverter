package com.example.simplecurrencyconverter.data.locale.preferences


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.simplecurrencyconverter.util.Constants
import com.example.simplecurrencyconverter.util.Constants.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import javax.inject.Inject
class DataStorePreference @Inject constructor(val dataStore:DataStore<Preferences>) :PreferencesInterface {


    override suspend fun saveBaseSymbol(baseSym: String) {
        dataStore.edit {
            it[Constants.BASE] = baseSym
        }
    }

    override fun getBaseSymbol(): Flow<String> = dataStore.data.map {

             it[Constants.BASE]?:"USD"
         }


    override suspend fun saveFromSymbol(fromSym: String) {
        dataStore.edit {
            it[Constants.FROM] = fromSym
        }
    }

    override fun getFromSymbol(): Flow<String> =
        dataStore.data.map {
            Log.d(TAG, "getFromSymbol: datastore")
            it[Constants.FROM]?:"USD"
           
        }.distinctUntilChanged()


    override suspend fun saveToSymbol(toSym: String) {
        dataStore.edit {
            it[Constants.TO] = toSym
        }
    }

    override fun getToSymbol(): Flow<String> =
        dataStore.data.map {
            Log.d(TAG, "getToSymbol: datastore")
            it[Constants.TO]?:"EUR"
        }.distinctUntilChanged()

}