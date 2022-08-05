package com.raffaellmir.exchangerate.presentation.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.presentation.currency.favorite.FavoriteState
import com.raffaellmir.exchangerate.presentation.currency.popular.PopularState
import com.raffaellmir.exchangerate.util.CurrencyListType
import com.raffaellmir.exchangerate.util.CurrencyListType.FAVORITE
import com.raffaellmir.exchangerate.util.CurrencyListType.POPULAR
import com.raffaellmir.exchangerate.util.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository,
) : ViewModel() {

    private val _popularState = MutableStateFlow(PopularState())
    val popularState = _popularState.asStateFlow()

    private val _favoriteState = MutableStateFlow(FavoriteState())
    val favoriteState = _favoriteState.asStateFlow()

    init {
        loadCurrencyList()
    }

    private fun loadCurrencyList() =
        viewModelScope.launch {
            repository.loadAllCurrencyBasedOn("USD").collect { result ->
                _popularState.value =
                    _popularState.value.copy(currencyList = result.data ?: emptyList())
                _favoriteState.value =
                    _favoriteState.value.copy(favoriteCurrencyList = result.data?.filter { it.favorite } ?: emptyList())
            }
        }

    private fun getCurrencyList(sortType: SortType? = null) {
        viewModelScope.launch {
            val currencyList = repository.getCurrencyList(sortType)

            _popularState.value = _popularState.value.copy(currencyList = currencyList)
        }
    }

    private fun getFavoriteCurrencyList(sortType: SortType? = null) {
        viewModelScope.launch {
            val currencyList = repository.getFavoriteCurrencyList(sortType)

            _favoriteState.value = _favoriteState.value.copy(favoriteCurrencyList = currencyList)
        }
    }

    fun onClickFavoriteButton(currency: Currency) {
        viewModelScope.launch {
            repository.changeFavoriteProperty(currency = currency)

            val currencyList = repository.getCurrencyList(_popularState.value.sortType)
            _popularState.value = _popularState.value.copy(currencyList = currencyList)

            val favoriteCurrencyList = repository.getFavoriteCurrencyList(_favoriteState.value.sortType)
            _favoriteState.value = _favoriteState.value.copy(favoriteCurrencyList = favoriteCurrencyList)
        }
    }

    fun onSortMenuItemClick(sortType: SortType, currencyListType: CurrencyListType): Boolean {
        when (currencyListType) {
            POPULAR -> {
                _popularState.value = _popularState.value.copy(sortType = sortType)
                getCurrencyList(sortType)
            }
            FAVORITE -> {
                _favoriteState.value = _favoriteState.value.copy(sortType = sortType)
                getFavoriteCurrencyList(sortType)
            }
        }
        return true
    }
}