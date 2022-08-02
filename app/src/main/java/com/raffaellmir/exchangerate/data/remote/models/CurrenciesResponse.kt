package com.raffaellmir.exchangerate.data.remote.models

import com.raffaellmir.exchangerate.domain.model.CurrencyItem

data class CurrenciesResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    val success: Boolean,
    val timestamp: Int
)

fun CurrenciesResponse.toCurrency() =
    this.rates.map { CurrencyItem(symbol = it.key, value = it.value, isFavorite = false) }

