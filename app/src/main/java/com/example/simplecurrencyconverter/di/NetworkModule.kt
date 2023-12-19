package com.example.simplecurrencyconverter.di

import com.example.simplecurrencyconverter.BuildConfig
import com.example.simplecurrencyconverter.data.remote.ApiInterface
import com.example.simplecurrencyconverter.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideApiInterface(): ApiInterface {
        val client =  OkHttpClient().newBuilder().addInterceptor(object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                var request : Request = chain.request();
                val url :HttpUrl = request.url()
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }

        }).build()

        return retrofit2.Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiInterface::class.java)
    }


}