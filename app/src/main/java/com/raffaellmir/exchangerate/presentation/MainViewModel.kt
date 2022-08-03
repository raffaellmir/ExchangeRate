package com.raffaellmir.exchangerate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import com.raffaellmir.exchangerate.domain.model.CurrencyItem
import com.raffaellmir.exchangerate.domain.model.PopularInfoState
import com.raffaellmir.exchangerate.util.toCurrency
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
    val popState = _popState.asStateFlow()

    init {
        loadCurrencyList()
    }

    private fun loadCurrencyList() {
        viewModelScope.launch {
            repository.loadAllCurrencyBasedOn("USD").collect {
                _popState.value = _popState.value.copy(currencyList = it.data ?: emptyList())
            }
        }
    }

    private fun getCurrencyList() {
        viewModelScope.launch {
            repository.getAllCurrency().collect { it ->
                _popState.value = _popState.value.copy(currencyList = it.map { it.toCurrency() })
            }
        }
    }

    fun onClickFavoriteButton(currencyItem: CurrencyItem) {
        viewModelScope.launch {
            repository.changeFavoriteProperty(currencyItem = currencyItem)
            getCurrencyList()
        }
    }
}