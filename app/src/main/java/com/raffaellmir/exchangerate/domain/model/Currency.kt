package com.raffaellmir.exchangerate.domain.model

import com.raffaellmir.exchangerate.data.local.CurrencyEntity

data class Currency(
    val symbol: String,
    val value: Double,
    val favorite: Boolean,
) {
    fun toCurrencyEntity(): CurrencyEntity = CurrencyEntity(symbol = symbol, value = value, favorite = favorite)
}