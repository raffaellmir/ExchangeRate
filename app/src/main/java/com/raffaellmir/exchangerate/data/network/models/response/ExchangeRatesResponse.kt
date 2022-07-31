package com.raffaellmir.exchangerate.data.network.models.response

import com.raffaellmir.exchangerate.data.network.models.Rates

data class ExchangeRatesResponse(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)