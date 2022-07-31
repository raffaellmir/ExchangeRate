package com.raffaellmir.exchangerate.data.network.api

import com.raffaellmir.exchangerate.Constants.CURRENCY_URL
import com.raffaellmir.exchangerate.data.network.TokenHolder
import com.raffaellmir.exchangerate.data.network.TokenHolder.currencyToken
import com.raffaellmir.exchangerate.data.network.models.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface CurrencyService {
    @GET("/latest/")
    suspend fun getExchangeRateBasedOn(
        @Url url: String = CURRENCY_URL,
        @Query("base") base: String,
        @Header("Authorization") token: String = currencyToken,
    ): Response<ExchangeRatesResponse>
}