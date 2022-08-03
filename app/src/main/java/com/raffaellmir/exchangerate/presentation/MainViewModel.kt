package com.raffaellmir.exchangerate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import com.raffaellmir.exchangerate.domain.model.Currency
import com.raffaellmir.exchangerate.presentation.currency.popular.PopularInfoState
import com.raffaellmir.exchangerate.util.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CurrencyRepository,
) : ViewModel() {

    private val _popState = MutableStateFlow(PopularInfoState())
    val popularState = _popState.asStateFlow()

    init { loadCurrencyList() }

    //main
    private fun loadCurrencyList() =
        viewModelScope.launch {
            repository.loadAllCurrencyBasedOn("USD").collect {
                _popState.value = _popState.value.copy(currencyList = it.data ?: emptyList())
            }
        }

    //popular
    fun getSortedCurrencyList(sortType: SortType): Boolean {
        viewModelScope.launch {
            val currencyList = repository.getSortedCurrencyList(sortType)
            _popState.value = _popState.value.copy(currencyList = currencyList, sortType = sortType)
        }
        return true
    }

    //main
    fun onClickFavoriteButton(currency: Currency) {
        viewModelScope.launch {
            repository.changeFavoriteProperty(currency = currency)
        }
        val listWithReplacedItem = _popState.value.currencyList.map {
            if (it.symbol == currency.symbol)
                Currency(symbol = it.symbol, value = it.value, favorite = !it.favorite)
            else it
        }
        _popState.value = _popState.value.copy(currencyList = listWithReplacedItem)
        //getSortedCurrencyList(_popState.value.sortType)
    }

}