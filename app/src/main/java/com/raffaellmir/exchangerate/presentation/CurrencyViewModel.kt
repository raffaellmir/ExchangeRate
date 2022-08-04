package com.raffaellmir.exchangerate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.presentation.currency.CurrencyListState
import com.raffaellmir.exchangerate.presentation.currency.favorite.FavoriteState
import com.raffaellmir.exchangerate.presentation.currency.popular.PopularState
import com.raffaellmir.exchangerate.util.CurrencyListType
import com.raffaellmir.exchangerate.util.CurrencyListType.*
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.Companion.getDefaultSortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository,
) : ViewModel() {

    private val _currencyListState = MutableStateFlow(CurrencyListState())
    val currencyListState = _currencyListState.asStateFlow()

    private val _popularState = MutableStateFlow(PopularState())
    val popularState = _popularState.asStateFlow()

    private val _favoriteState = MutableStateFlow(FavoriteState())
    val favoriteState = _favoriteState.asStateFlow()

    init {
        loadCurrencyList()
    }

    private fun loadCurrencyList() =
        viewModelScope.launch {
            repository.loadAllCurrencyBasedOn("USD").collect {
                _currencyListState.value = _currencyListState.value.copy(currencyList = it.data ?: emptyList())
            }
        }

    fun getCurrencyList(sortType: SortType? = null) {
        viewModelScope.launch {
            val currencyList = repository.getCurrencyList(sortType)

            _currencyListState.value = _currencyListState.value.copy(currencyList = currencyList)
        }
    }

    fun onClickFavoriteButton(currency: Currency) {
        viewModelScope.launch {
            repository.changeFavoriteProperty(currency = currency)
        }
        val listWithReplacedItem = _currencyListState.value.currencyList.map {
            if (it.symbol == currency.symbol) Currency(symbol = it.symbol, value = it.value, favorite = !it.favorite)
            else it
        }

        _currencyListState.value = _currencyListState.value.copy(currencyList = listWithReplacedItem)
    }

    fun onSortMenuItemClick(sortType: SortType, currencyListType: CurrencyListType): Boolean {
        when (currencyListType) {
            POPULAR -> _popularState.value = _popularState.value.copy(sortType = sortType)
            FAVORITE -> _favoriteState.value = _favoriteState.value.copy(sortType = sortType)
        }
        getCurrencyList(sortType)
        return true
    }
}