package com.raffaellmir.exchangerate.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.raffaellmir.exchangerate.data.local.CurrencyDao
import com.raffaellmir.exchangerate.data.local.CurrencyEntity
import com.raffaellmir.exchangerate.data.remote.api.CurrencyService
import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.Event
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.Companion.getDefaultSortType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val currencyService: CurrencyService,
    private val currencyDao: CurrencyDao,
) {
    fun loadCurrencyList() = flow {
        emit(Event.Loading())

        val currencyList = currencyDao.getCurrencyList(getDefaultSortType().type).map { it.toCurrency() }
        emit(Event.Loading(currencyList))

        try {
            val response = currencyService.getCurrencyBasedOn(base = getBaseCurrency())

            currencyDao.deleteCurrencyList(response.rates.map { it.key })
            currencyDao.insertCurrencyList(response.rates.map {
                CurrencyEntity(
                    symbol = it.key, value = it.value,
                    favorite = currencyDao.getCurrencyBySymbol(it.key)?.favorite ?: false)
            })

        } catch (e: IOException) {
            emit(
                Event.Error(
                    error = "Couldn't reach server, check your internet connection.",
                    data = currencyList
                )
            )
        }

        val newCurrencyList = currencyDao.getCurrencyList(getDefaultSortType().type).map { it.toCurrency() }
        emit(Event.Success(newCurrencyList))
    }.flowOn(Dispatchers.IO)

    suspend fun getCurrencyList(sortType: SortType?) =
        currencyDao
            .getCurrencyList(sortType = sortType?.type ?: getDefaultSortType().type)
            .map { it.toCurrency() }

    suspend fun getFavoriteCurrencyList(sortType: SortType?) =
        currencyDao
            .getFavoriteCurrencyList(sortType = sortType?.type ?: getDefaultSortType().type)
            .map { it.toCurrency() }

    suspend fun changeFavoriteProperty(currency: Currency) = withContext(Dispatchers.IO) {
        try {
            val currencyEntity = currency.copy(favorite = !currency.favorite).toCurrencyEntity()
            currencyDao.updateCurrency(currencyEntity)
        } catch (e: Exception) { }
    }

    private val baseCurrencyData: SharedPreferences = context.getSharedPreferences("BASE_CURRENCY", Context.MODE_PRIVATE)

    fun getBaseCurrency() = baseCurrencyData.getString(BASE_CURRENCY_KEY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY

    fun changeBaseCurrency(currency: String) { baseCurrencyData.edit { putString(BASE_CURRENCY_KEY, currency) } }

    companion object {
        const val DEFAULT_CURRENCY = "USD"
        const val BASE_CURRENCY_KEY = "BASE_CURRENCY"
    }
}