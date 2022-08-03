package com.raffaellmir.exchangerate.data.remote.models

data class CurrenciesResponse(
    val base: String,
    val date: String,
    val rates: HashMap<String, Double>,
    val success: Boolean,
    val timestamp: Int
)

