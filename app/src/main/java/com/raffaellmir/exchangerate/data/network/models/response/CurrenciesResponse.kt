package com.raffaellmir.exchangerate.data.network.models.response

data class CurrenciesResponse(
    val success: Boolean,
    val symbols: Map<String, String>
)