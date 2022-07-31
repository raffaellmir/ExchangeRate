package com.raffaellmir.exchangerate.data.network.models.response

import com.google.gson.annotations.SerializedName
import com.raffaellmir.exchangerate.data.network.models.Rates

data class ExchangeRatesResponse(
    @SerializedName("rates") val rates: Map<String, Double>?,
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("timestamp") val timestamp: Int
)