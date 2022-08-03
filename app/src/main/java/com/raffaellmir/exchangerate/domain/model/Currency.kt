package com.raffaellmir.exchangerate.domain.model

data class Currency(
    val symbol: String,
    val value: Double,
    val favorite: Boolean,
)