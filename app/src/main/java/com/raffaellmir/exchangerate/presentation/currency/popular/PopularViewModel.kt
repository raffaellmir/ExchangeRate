package com.raffaellmir.exchangerate.presentation.currency.popular

import androidx.lifecycle.ViewModel
import com.raffaellmir.exchangerate.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CurrencyRepository,
) : ViewModel() {


}