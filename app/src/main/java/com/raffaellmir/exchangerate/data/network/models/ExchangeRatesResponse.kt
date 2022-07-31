package com.raffaellmir.exchangerate.data.network.models

data class ExchangeRatesResponse(
    val base: String,
    val date: String,
    val rates: List<Map<String, Double>>,
    val success: Boolean,
    val timestamp: Int
)