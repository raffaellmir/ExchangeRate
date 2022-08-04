package com.raffaellmir.exchangerate.data.local

import androidx.room.*
import com.raffaellmir.exchangerate.util.SortType
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyList(currencyList: List<CurrencyEntity>)

    @Query("DELETE FROM currency WHERE symbol IN(:currencyList) AND favorite = 0")
    suspend fun deleteCurrencyList(currencyList: List<String>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrency(currencyEntity: CurrencyEntity)

    @Query("SELECT * FROM currency ORDER BY symbol ASC")
    suspend fun getAllCurrency(): List<CurrencyEntity>

    @Query("SELECT * FROM currency WHERE symbol = :symbol")
    suspend fun getCurrencyBySymbol(symbol: String): CurrencyEntity?

    @Query("SELECT * FROM currency ORDER BY " +
           "CASE WHEN :sortType = 0 THEN symbol END ASC, " +
           "CASE WHEN :sortType = 1 THEN symbol END DESC, " +
           "CASE WHEN :sortType = 2 THEN value END ASC, " +
           "CASE WHEN :sortType = 3 THEN value END DESC ")
    suspend fun getPopularCurrencyList(sortType: Int): List<CurrencyEntity>

    @Query("SELECT * FROM currency WHERE favorite = 1 ORDER BY " +
            "CASE WHEN :sortType = 0 THEN symbol END ASC, " +
            "CASE WHEN :sortType = 1 THEN symbol END DESC, " +
            "CASE WHEN :sortType = 2 THEN value END ASC, " +
            "CASE WHEN :sortType = 3 THEN value END DESC ")
    suspend fun getFavoriteCurrencyList(sortType: Int): List<CurrencyEntity>
}