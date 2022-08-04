package com.raffaellmir.exchangerate.presentation.currency.favorite

import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.*

data class FavoriteState(
    val baseCurrency: String = "USD",
    val sortType: SortType = NAME_ASC
)