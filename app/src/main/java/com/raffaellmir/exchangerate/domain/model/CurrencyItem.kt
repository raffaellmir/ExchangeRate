package com.raffaellmir.exchangerate.domain.model

data class CurrencyItem(
    val symbol: String,
    var value: Double,
    var isFavorite: Boolean,
)