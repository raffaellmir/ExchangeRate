package com.raffaellmir.exchangerate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raffaellmir.exchangerate.domain.model.CurrencyItem

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey
    val symbol: String,
    val value: Double,
    val isFavorite: Boolean
)

fun CurrencyEntity.toCurrency() =
    CurrencyItem(symbol = this.symbol, value = this.value, isFavorite = this.isFavorite)