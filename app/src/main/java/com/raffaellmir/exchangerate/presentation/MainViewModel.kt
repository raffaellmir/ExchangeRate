package com.raffaellmir.exchangerate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import com.raffaellmir.exchangerate.domain.model.PopularInfoState
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
        getCurrencyList()
    }

    private fun getCurrencyList() {
        viewModelScope.launch {
            repository.loadAllCurrencyBasedOn("USD").collect {
                _popState.value = _popState.value.copy(currencyList = it.data ?: emptyList())
            }
        }
    }
}