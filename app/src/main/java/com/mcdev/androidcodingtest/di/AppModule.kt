package com.mcdev.androidcodingtest.di

import com.mcdev.androidcodingtest.main.DefaultRepository
import com.mcdev.androidcodingtest.main.DispatcherProvider
import com.mcdev.androidcodingtest.main.MainRepository
import com.mcdev.androidcodingtest.model.CurrencyApi
import com.mcdev.androidcodingtest.utils.API_KEY
import com.mcdev.androidcodingtest.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = DefaultRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }
}