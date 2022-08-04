package com.raffaellmir.exchangerate.presentation.currency.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.Companion.getDefaultSortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: CurrencyRepository,
) : ViewModel() {

    private val _favState = MutableStateFlow(FavoriteInfoState())
    val favoriteState = _favState.asStateFlow()

    init {
        getCurrencyList()
    }

    fun getCurrencyList(sortType: SortType? = null): Boolean {
        viewModelScope.launch {
            val currencyList = repository.getFavoriteCurrencyList(sortType)
            _favState.value = _favState.value.copy(currencyList = currencyList, sortType = sortType ?: getDefaultSortType())
        }
        return true
    }

    fun onClickFavoriteButton(currency: Currency) {
        viewModelScope.launch {
            repository.changeFavoriteProperty(currency = currency)
        }
        val listWithReplacedItem = _favState.value.currencyList.map {
            if (it.symbol == currency.symbol) Currency(symbol = it.symbol, value = it.value, favorite = !it.favorite)
            else it
        }

        _favState.value = _favState.value.copy(currencyList = listWithReplacedItem)
    }
}