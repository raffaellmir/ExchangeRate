package com.raffaellmir.exchangerate.domain.model

import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.NAME_ASC

data class PopularInfoState (
    val baseCurrency: String = "USD",
    val currencyList: List<Currency> = emptyList(),
    val sortType: SortType = NAME_ASC
)