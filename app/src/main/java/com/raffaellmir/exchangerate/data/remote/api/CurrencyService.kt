package com.raffaellmir.exchangerate.data.remote.api

import com.raffaellmir.exchangerate.Constants.CURRENCY_TOKEN
import com.raffaellmir.exchangerate.data.remote.models.CurrenciesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyService {
    @GET("exchangerates_data/latest")
    suspend fun getAllCurrencyBasedOn(
        @Header("apikey") token: String = CURRENCY_TOKEN,
        @Query("symbols") symbols: String? = null,
        @Query("base") base: String? = null,
    ): Response<CurrenciesResponse>
}