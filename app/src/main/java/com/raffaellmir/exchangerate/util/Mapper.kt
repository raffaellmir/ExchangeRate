package com.raffaellmir.exchangerate.util

import com.raffaellmir.exchangerate.data.local.CurrencyEntity
import com.raffaellmir.exchangerate.domain.model.CurrencyItem

fun CurrencyEntity.toCurrency() =
    CurrencyItem(symbol = this.symbol, value = this.value, isFavorite = this.isFavorite)

fun CurrencyItem.toCurrencyEntity() =
    CurrencyEntity(symbol = this.symbol, value = this.value, isFavorite = this.isFavorite)