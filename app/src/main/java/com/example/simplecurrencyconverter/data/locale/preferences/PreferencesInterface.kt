package com.example.simplecurrencyconverter.data.locale.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesInterface {

    suspend fun saveBaseSymbol(baseSym :String)
    fun getBaseSymbol():Flow<String>

    suspend fun saveFromSymbol(fromSym :String)
    fun getFromSymbol():Flow<String>

    suspend fun saveToSymbol(toSym :String)
    fun getToSymbol():Flow<String>
}