package com.raffaellmir.exchangerate.data.network.models.response

import com.raffaellmir.exchangerate.data.network.models.Symbols

data class Currencies(
    val success: Boolean,
    val symbols: Symbols
)