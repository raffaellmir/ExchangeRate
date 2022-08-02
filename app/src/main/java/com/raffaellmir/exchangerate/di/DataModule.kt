package com.raffaellmir.exchangerate.di

import android.content.Context
import androidx.room.Room
import com.raffaellmir.exchangerate.data.local.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    @Provides
    fun provideCurrencyDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CurrencyDatabase::class.java, "currency").build()

    @Provides
    fun provideCurrencyDao(currencyDatabase: CurrencyDatabase) = currencyDatabase.currencyDao()
}