package com.raffaellmir.exchangerate.presentation.currency.popular

import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.NAME_ASC

data class PopularInfoState (
    val baseCurrency: String = "USD",
    val currencyList: List<Currency> = emptyList(),
    val sortType: SortType = NAME_ASC
)