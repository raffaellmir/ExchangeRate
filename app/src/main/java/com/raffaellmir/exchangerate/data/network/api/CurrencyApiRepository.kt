package com.raffaellmir.exchangerate.data.network.api

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyApiRepository @Inject constructor(
    private val currencyService: CurrencyService
) {
    suspend fun getExchangeRateBasedOn(base: String) = flow {
        val response = currencyService.getExchangeRateBasedOn(base = base)

        if (response.isSuccessful)
            emit(response.body()!!)
    }
}