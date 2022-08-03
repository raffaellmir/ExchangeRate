package com.raffaellmir.exchangerate.presentation.currency.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import com.raffaellmir.exchangerate.util.SortType
import com.raffaellmir.exchangerate.util.SortType.DEFAUT
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
            _favState.value =
                _favState.value.copy(currencyList = currencyList, sortType = sortType ?: DEFAUT)
        }
        return true
    }
}