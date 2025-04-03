package com.example.cleanarchitectureproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cleanarchitectureproject.data.local.portfolio.PortfolioDao
import com.example.cleanarchitectureproject.data.local.portfolio.PortfolioEntity
import com.example.cleanarchitectureproject.data.local.saved_coins.CryptoCoinEntity
import com.example.cleanarchitectureproject.data.local.saved_coins.CryptoDao
import com.example.cleanarchitectureproject.data.local.transactions.TransactionsDao
import com.example.cleanarchitectureproject.data.local.transactions.TransactionsEntity

@Database(entities = [CryptoCoinEntity::class, PortfolioEntity::class,TransactionsEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)  // Add this line
abstract class CryptoDatabase : RoomDatabase() {
    abstract val cryptoDao: CryptoDao
    abstract val portfolioDao: PortfolioDao
    abstract val transactionsDao: TransactionsDao
}
