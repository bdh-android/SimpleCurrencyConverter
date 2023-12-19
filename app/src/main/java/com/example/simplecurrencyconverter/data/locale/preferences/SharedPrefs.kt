package com.example.simplecurrencyconverter.data.locale.preferences


import android.content.SharedPreferences
import com.example.simplecurrencyconverter.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SharedPrefs @Inject constructor(val prefs: SharedPreferences) : PreferencesInterface {



    override suspend fun saveBaseSymbol(baseSym: String) {
        prefs.edit().putString(Constants.S_BASE,baseSym).apply()
    }

    override fun getBaseSymbol(): Flow<String> = flow {
        emit (prefs.getString(Constants.S_BASE,"USD")?:"USD")
    }

    override suspend fun saveFromSymbol(fromSym: String) {
        prefs.edit().putString(Constants.S_FROM,fromSym).apply()
    }

    override fun getFromSymbol(): Flow<String> =flow{
         emit(prefs.getString(Constants.S_FROM,"USD")?:"USD")
    }

    override suspend fun saveToSymbol(toSym: String) {
        prefs.edit().putString(Constants.S_TO,toSym).apply()
    }

    override fun getToSymbol(): Flow<String> =flow{
        emit(prefs.getString(Constants.S_TO,"EUR")?:"EUR")
    }
}