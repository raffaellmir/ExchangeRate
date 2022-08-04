package com.raffaellmir.exchangerate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raffaellmir.exchangerate.domain.model.Currency

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey
    val symbol: String,
    val value: Double,
    val favorite: Boolean
) {
    fun toCurrency(): Currency = Currency(symbol = symbol, value = value, favorite = favorite)
}