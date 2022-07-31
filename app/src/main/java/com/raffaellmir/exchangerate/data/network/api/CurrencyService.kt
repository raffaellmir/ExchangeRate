package com.raffaellmir.exchangerate.data.network.api

import com.raffaellmir.exchangerate.data.network.TokenHolder
import com.raffaellmir.exchangerate.data.network.models.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface CurrencyService {
    @GET("/latest/")
    suspend fun getExchangeRateBasedOn(
        @Url url: String = "https://api.apilayer.com/exchangerates_data/",
        @Query("base") base: String,
        @Header("Authorization") token: String = TokenHolder.currencyToken,
    ): Response<ExchangeRatesResponse>
}