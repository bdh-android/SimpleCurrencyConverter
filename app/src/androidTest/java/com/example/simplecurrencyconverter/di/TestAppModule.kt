package com.example.simplecurrencyconverter.di

import android.content.Context

import androidx.room.Room
import com.example.simplecurrencyconverter.data.AndroidiFakeCurrencyRepository
import com.example.simplecurrencyconverter.data.CurrencyRepository

import com.example.simplecurrencyconverter.data.locale.database.CurrencyDatabase

import dagger.Module
import dagger.Provides

import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher

import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {
    @Provides
    @Named("database_test")
    fun provideDataBase(@ApplicationContext context: Context)=
        Room.inMemoryDatabaseBuilder(context, CurrencyDatabase::class.java )
            .allowMainThreadQueries()
            .build()
    @Singleton
    @Provides
    fun provideRepository( @MainDispatcher dispatcher: CoroutineDispatcher
    ): CurrencyRepository {
        return AndroidiFakeCurrencyRepository( dispatcher)
    }


}