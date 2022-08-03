package com.raffaellmir.exchangerate.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyList(currencyList: List<CurrencyEntity>)

    @Query("DELETE FROM currency WHERE symbol IN(:currencyList) AND isFavorite = 0")
    suspend fun deleteCurrencies(currencyList: List<String>)

    @Update
    suspend fun updateCurrency(currencyEntity: CurrencyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrencyList(currencyList: List<CurrencyEntity>)

    @Query("SELECT * FROM currency ORDER BY symbol ASC")
    fun getAllCurrencyFlow(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currency ORDER BY symbol ASC")
    suspend fun getAllCurrency(): List<CurrencyEntity>

    @Query("SELECT * FROM currency WHERE symbol = :symbol")
    suspend fun getCurrencyBySymbol(symbol: String): CurrencyEntity?

    @Query("SELECT * FROM currency ORDER BY " +
            "CASE WHEN :asc = 0 THEN symbol END DESC, " +
            "CASE WHEN :asc = 1 THEN symbol END ASC ")
    fun getAllSortedByName(asc : Int): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currency ORDER BY " +
            "CASE WHEN :asc = 0 THEN value END DESC, " +
            "CASE WHEN :asc = 1 THEN value END ASC ")
    fun getAllSortedByValue(asc : Int): Flow<List<CurrencyEntity>>
}