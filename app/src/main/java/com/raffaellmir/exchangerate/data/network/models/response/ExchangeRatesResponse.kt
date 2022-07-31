package com.raffaellmir.exchangerate.data.network.models.response

import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    val success: Boolean,
    val timestamp: Int
)