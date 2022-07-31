package com.raffaellmir.exchangerate.data.network.models.response

data class ExchangeRatesResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    val success: Boolean,
    val timestamp: Int
)