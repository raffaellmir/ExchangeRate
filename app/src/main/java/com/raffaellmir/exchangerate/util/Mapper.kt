package com.raffaellmir.exchangerate.util

import com.raffaellmir.exchangerate.data.local.CurrencyEntity
import com.raffaellmir.exchangerate.domain.model.Currency

fun CurrencyEntity.toCurrency() =
    Currency(symbol = this.symbol, value = this.value, favorite = this.favorite)

fun Currency.toCurrencyEntity() =
    CurrencyEntity(symbol = this.symbol, value = this.value, favorite = this.favorite)

fun Boolean.toInt() = if (this) 1 else 0