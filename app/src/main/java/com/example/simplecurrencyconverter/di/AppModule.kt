package com.example.simplecurrencyconverter.di


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.simplecurrencyconverter.data.CurrencyRepository
import com.example.simplecurrencyconverter.data.CurrencyRepositoryImpl
import com.example.simplecurrencyconverter.data.DataSource

import com.example.simplecurrencyconverter.data.remote.ApiInterface
import com.example.simplecurrencyconverter.data.locale.database.CurrencyDatabase
import com.example.simplecurrencyconverter.data.locale.database.LocalDataSource
import com.example.simplecurrencyconverter.data.locale.preferences.DataStorePreference
import com.example.simplecurrencyconverter.data.locale.preferences.PreferencesInterface
import com.example.simplecurrencyconverter.data.remote.RemoteDataSource
import com.example.simplecurrencyconverter.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.*


@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CurrencyDatabase {
        return Room.databaseBuilder(context, CurrencyDatabase::class.java,Constants.Name)
            .build()
    }

    @Singleton
    @Provides
    fun provideRepository(@RemoteDataSourceQualifier remote: DataSource
                          ,@LocalDataSourceQualifier local: DataSource,
                          @DataStoreQualifier  prefs: PreferencesInterface, @IODispatcher dispatcher:CoroutineDispatcher):CurrencyRepository{
        return CurrencyRepositoryImpl(remote, local,prefs,dispatcher)
    }
    @Singleton
    @Provides
    @LocalDataSourceQualifier
    fun provideLocalDataSource(database :CurrencyDatabase ):DataSource{
        return LocalDataSource(database.currencyDao())
    }
    @Singleton
    @Provides
    @RemoteDataSourceQualifier
    fun provideRemoteDataSource(api :ApiInterface ):DataSource{
        return RemoteDataSource(api)
    }


    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context:Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            migrations = listOf(SharedPreferencesMigration(context,Constants.PREFS_NANE)),
            produceFile =  {context.preferencesDataStoreFile(Constants.PREFS_NANE)}
        )
    }
    @Singleton
    @Provides
    fun provideSharedPrefernces(@ApplicationContext context: Context):SharedPreferences{
        return context.getSharedPreferences(Constants.PREFS_NANE,MODE_PRIVATE)
    }

    @Singleton
    @Provides
    @DataStoreQualifier
    fun provideDataStorePreferences(dataStore:DataStore<Preferences>):PreferencesInterface{
       return DataStorePreference(dataStore)
    }
/*
    @Singleton
    @Provides
    @SharedPrefsQualifier
    fun provideSharedPrefs(sharedPreferences: SharedPreferences): PreferencesInterface {
        return SharedPrefs(sharedPreferences)
    }*/

}