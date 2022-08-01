package com.raffaellmir.exchangerate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.network.api.CurrencyApiRepository
import com.raffaellmir.exchangerate.domain.model.CurrencyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CurrencyApiRepository,
) : ViewModel() {

    private val _currencyList = MutableStateFlow<List<CurrencyItem>?>(null)
    val currencyList = _currencyList.asStateFlow()

    init {
        getCurrencyList()
    }

    private fun getCurrencyList() {
        viewModelScope.launch {
            repository.getExchangeRateBasedOn("USD").collect { response ->
                val list = response.rates.map { CurrencyItem(it.key, it.value, false) }

                _currencyList.value = list
            }
        }
    }
}