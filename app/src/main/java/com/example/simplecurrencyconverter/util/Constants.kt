package com.example.simplecurrencyconverter.util

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    const val TAG = "currency converter->"
    const val BASE_URL = "https://api.forexrateapi.com/"
    const val Name = "currency_database"
    const val S_BASE = "base"
    const val S_FROM="from"
    const val S_TO="to"
    const val PREFS_NANE = "prefs"

    val BASE = stringPreferencesKey(S_BASE)
    val FROM = stringPreferencesKey(S_FROM)
    val TO = stringPreferencesKey(S_TO)
}