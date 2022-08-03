package com.raffaellmir.exchangerate.data.repository

import com.raffaellmir.exchangerate.data.local.CurrencyDao
import com.raffaellmir.exchangerate.data.local.CurrencyEntity
import com.raffaellmir.exchangerate.data.remote.api.CurrencyService
import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.*
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
    suspend fun loadAllCurrencyBasedOn(base: String) = flow {
        emit(Event.Loading())

        val currencyList = currencyDao.getAllCurrency().map { it.toCurrency() }
        emit(Event.Loading(currencyList))

        try {
            val response = currencyService.getAllCurrencyBasedOn(base = base)

            currencyDao.deleteCurrencies(response.body()!!.rates.map { it.key })
            currencyDao.insertCurrencyList(response.body()!!.rates.map {
                CurrencyEntity(symbol = it.key, value = it.value,
                    favorite = currencyDao.getCurrencyBySymbol(it.key)?.favorite ?: false) })

        } catch (e: IOException) {
            emit(Event.Error(
                error = "Couldn't reach server, check your internet connection.",
                data = currencyList
            ))
        }

        val newCurrencyList = currencyDao.getAllCurrency().map { it.toCurrency() }
        emit(Event.Success(newCurrencyList))
    }.flowOn(Dispatchers.IO)

    suspend fun getSortedCurrencyList(sortType: SortType) =
        currencyDao.getSortedCurrencyList(sortType.type).map { it.toCurrency() }

    suspend fun changeFavoriteProperty(currency: Currency) = withContext(Dispatchers.IO) {
        try {
            val currencyEntity = currency.copy(favorite = !currency.favorite).toCurrencyEntity()
            currencyDao.updateCurrency(currencyEntity)
        } catch (e: Exception) {}
    }
}