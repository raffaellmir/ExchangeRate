package com.raffaellmir.exchangerate.data.remote.api

import com.raffaellmir.exchangerate.data.remote.models.CurrenciesResponse
import com.raffaellmir.exchangerate.util.Constants.CURRENCY_TOKEN
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyService {
    @GET("exchangerates_data/latest")
    suspend fun getCurrencyBasedOn(
        @Header("apikey") token: String = CURRENCY_TOKEN,
        @Query("base") base: String? = null,
    ): CurrenciesResponse
}