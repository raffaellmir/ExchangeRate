package com.raffaellmir.exchangerate.presentation.currency.popular

import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.NAME_ASC

data class PopularState (
    val baseCurrency: String = "USD",
    val sortType: SortType = NAME_ASC,
    val currencyList: List<Currency> = emptyList(),
)
