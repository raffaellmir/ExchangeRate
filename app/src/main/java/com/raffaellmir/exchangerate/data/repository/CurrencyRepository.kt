package com.raffaellmir.exchangerate.data.repository

import com.raffaellmir.exchangerate.data.local.CurrencyDao
import com.raffaellmir.exchangerate.data.local.CurrencyEntity
import com.raffaellmir.exchangerate.data.remote.api.CurrencyService
import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.Event
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.Companion.getDefaultSortType
import com.raffaellmir.exchangerate.util.SortType.NAME_ASC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyService: CurrencyService,
    private val currencyDao: CurrencyDao,
) {
    fun loadAllCurrencyBasedOn(base: String) = flow {
        emit(Event.Loading())

        val currencyList = currencyDao.getCurrencyList(getDefaultSortType().type).map { it.toCurrency() }
        emit(Event.Loading(currencyList))

        try {
            val response = currencyService.getCurrencyBasedOn(base = base)

            currencyDao.deleteCurrencyList(response.rates.map { it.key })
            currencyDao.insertCurrencyList(response.rates.map {
                CurrencyEntity(
                    symbol = it.key, value = it.value, favorite = currencyDao.getCurrencyBySymbol(it.key)?.favorite ?: false) })

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
        } catch (e: Exception) {
        }
    }
}