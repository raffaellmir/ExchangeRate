package com.raffaellmir.exchangerate.presentation.currency

import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.CurrencyListType
import com.raffaellmir.exchangerate.util.CurrencyListType.*
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.NAME_ASC

data class CurrencyState(
    val currencyList: List<Currency> = emptyList(),
    val baseCurrency: String = "USD",
    val currencyListType: CurrencyListType = POPULAR,
    val sortType: SortType = NAME_ASC,
)
