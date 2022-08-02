package com.raffaellmir.exchangerate.domain.model

data class CurrencyItem(
    val symbol: String,
    val value: Double,
    val isFavorite: Boolean,
)