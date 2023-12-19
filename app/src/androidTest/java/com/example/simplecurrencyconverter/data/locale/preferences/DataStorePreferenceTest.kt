package com.example.simplecurrencyconverter.data.locale.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences

import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simplecurrencyconverter.di.AppModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

private const val PREFS_NANE = "testdatastore"

@UninstallModules(AppModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class DataStorePreferenceTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    val context: Context = ApplicationProvider.getApplicationContext()
    val dispatcher :CoroutineDispatcher = StandardTestDispatcher()
    val job = Job()
    val testScope = TestScope(dispatcher + job)

    @BindValue
    val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create (
       scope=testScope,
       produceFile =  {context.preferencesDataStoreFile(PREFS_NANE)}
   )
   lateinit var dataStorePreference :DataStorePreference

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        Dispatchers.setMain(dispatcher)

        dataStorePreference= DataStorePreference(testDataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

        File(context.filesDir,"datastore").deleteRecursively()
        testScope.cancel()
    }

    @Test
    fun fetchBaseInitialPreferences() {
        testScope.runTest {
            assertEquals(
                "initial base symbol is USD",
                dataStorePreference.getBaseSymbol().first(),
                "USD"
            )

        }
    }
    @Test
    fun fetchFromInitialPreferences() {
        testScope.runTest {
            assertEquals(
                "initial From symbol is USD",
                dataStorePreference.getFromSymbol().first(),
                "USD"
            )

        }
    }
    @Test
    fun fetchToInitialPreferences() {
        testScope.runTest {
            assertEquals(
                "initial to symbol is EUR",
                dataStorePreference.getToSymbol().first(),
                "EUR"
            )

        }
    }

    @Test
    fun changeBaseSymbol() {
        testScope.runTest {
            dataStorePreference.saveBaseSymbol("GBP")
            assertEquals("change base symbol", dataStorePreference.getBaseSymbol().first(), "GBP")


        }
    }
    @Test
    fun changeFromSymbol() {
        testScope.runTest {
            dataStorePreference.saveFromSymbol("GBP")
            assertEquals("change from symbol", dataStorePreference.getFromSymbol().first(), "GBP")


        }
    }
    @Test
    fun changeToSymbol() {
        testScope.runTest {
            dataStorePreference.saveToSymbol("GBP")
            assertEquals("change to symbol", dataStorePreference.getToSymbol().first(), "GBP")


        }
    }
}