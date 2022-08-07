package com.raffaellmir.exchangerate.presentation.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import com.raffaellmir.exchangerate.domain.model.Currency
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
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {

    private val _currencyState = MutableStateFlow(CurrencyState())
    val currencyState = _currencyState.asStateFlow()

    private val _baseCurrencyList = MutableStateFlow<List<String>>(emptyList())
    val baseCurrencyList = _baseCurrencyList.asStateFlow()

    init {
        loadCurrencyList()
    }

    private fun loadCurrencyList() {
        viewModelScope.launch {
            currencyRepository.loadCurrencyList().collect { result ->
                getCurrencyList()
                _baseCurrencyList.value = result.data?.map { it.symbol } ?: emptyList()
            }
        }
    }

    private fun getCurrencyList() {
        val sortType = _currencyState.value.sortType
        val currencyListType = _currencyState.value.currencyListType
        _currencyState.value = _currencyState.value.copy(baseCurrency = currencyRepository.getBaseCurrency())

        viewModelScope.launch {
            when (currencyListType) {
                POPULAR -> {
                    val currencyList = currencyRepository.getCurrencyList(sortType)
                    _currencyState.value = _currencyState.value.copy(currencyList = currencyList)
                }
                FAVORITE -> {
                    val currencyList = currencyRepository.getFavoriteCurrencyList(sortType)
                    _currencyState.value = _currencyState.value.copy(currencyList = currencyList)
                }
            }
        }
    }

    fun onFavoriteChange(currency: Currency) {
        viewModelScope.launch {
            currencyRepository.changeFavoriteProperty(currency = currency)
            getCurrencyList()
        }
    }

    fun onSortChange(sortType: SortType): Boolean {
        _currencyState.value = _currencyState.value.copy(sortType = sortType)
        getCurrencyList()
        return true
    }

    fun onListChange(currencyListType: CurrencyListType) {
        _currencyState.value = _currencyState.value.copy(currencyListType = currencyListType)
        getCurrencyList()
    }

    fun onBaseCurrencyChange(baseCurrencySymbol: String) {
        currencyRepository.changeBaseCurrency(baseCurrencySymbol)
        _currencyState.value = _currencyState.value.copy(baseCurrency = baseCurrencySymbol)
        loadCurrencyList()
    }
}