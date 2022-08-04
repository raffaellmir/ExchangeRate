package com.raffaellmir.exchangerate.presentation.currency

import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.SortType

data class CurrencyListState(
    val currencyList: List<Currency> = emptyList(),
) {
    val favoriteCurrencyListTest: List<Currency>
        get() = currencyList.filter { it.favorite }
}