package com.raffaellmir.exchangerate.domain.model

data class PopularInfoState (
    val baseCurrency: String = "USD",
    val currencyList: List<CurrencyItem> = emptyList(),
)