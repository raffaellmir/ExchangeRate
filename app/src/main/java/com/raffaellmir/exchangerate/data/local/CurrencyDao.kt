package com.raffaellmir.exchangerate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert
    suspend fun insertCurrencyList(currencyList: List<CurrencyEntity>)

    @Query("DELETE FROM currency WHERE symbol IN(:currencyList)")
    suspend fun deleteCurrencies(currencyList: List<String>)

    @Query("SELECT * FROM currency")
    fun getAllCurrencyFlow(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currency")
    fun getAllCurrency(): List<CurrencyEntity>

    @Query("SELECT * FROM currency ORDER BY " +
            "CASE WHEN :isAsc = 0 THEN symbol END DESC, " +
            "CASE WHEN :isAsc = 1 THEN symbol END ASC ")
    fun getAllSortedByName(isAsc : Int): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currency ORDER BY " +
            "CASE WHEN :isAsc = 0 THEN value END DESC, " +
            "CASE WHEN :isAsc = 1 THEN value END ASC ")
    fun getAllSortedByValue(isAsc : Int): Flow<List<CurrencyEntity>>
}