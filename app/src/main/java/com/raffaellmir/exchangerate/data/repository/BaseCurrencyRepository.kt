package com.raffaellmir.exchangerate.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseCurrencyRepository @Inject constructor(@ApplicationContext private val context: Context) {

    val baseCurrencyFlow: Flow<String> = context.datastore.data.map { preferences ->
        preferences[BASE_CURRENCY_KEY] ?: DEFAULT_BASE_CURRENCY
    }

    suspend fun setBaseCurrency(baseCurrency: String) {
        context.datastore.edit { preferences ->
            preferences[BASE_CURRENCY_KEY] = baseCurrency
        }
    }

    companion object {
        private val Context.datastore by preferencesDataStore(name = "data")

        const val DEFAULT_BASE_CURRENCY = "USD"
        val BASE_CURRENCY_KEY = stringPreferencesKey("BASE_CURRENCY")
    }
}