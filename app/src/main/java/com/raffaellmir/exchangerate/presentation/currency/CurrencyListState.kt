package com.raffaellmir.exchangerate.presentation.currency

import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.SortType

class CurrencyListState(
    val baseCurrency: String = "USD",
    val currencyList: List<Currency> = emptyList(),
    val sortType: SortType = SortType.NAME_ASC,
) {
    val favoriteCurrencyListTest: List<Currency>
        get() = currencyList.filter { it.favorite }
}